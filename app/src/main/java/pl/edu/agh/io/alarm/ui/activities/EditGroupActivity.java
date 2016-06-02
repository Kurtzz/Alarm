package pl.edu.agh.io.alarm.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.sqlite.helper.DatabaseHelper;
import pl.edu.agh.io.alarm.sqlite.model.Friend;
import pl.edu.agh.io.alarm.sqlite.model.Group;
import pl.edu.agh.io.alarm.ui.Constants;
import pl.edu.agh.io.alarm.ui.adapters.DefaultFriendListAdapter;

public class EditGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spinner;
    private Group group;
    private Button saveButton;
    private Button inviteFriendsButton;
    private ListView friendList;
    private DefaultFriendListAdapter friendListAdapter;

    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        helper = new DatabaseHelper(getApplicationContext());

        group = helper.getGroup(getIntent().getStringExtra(Constants.EXTRA_ID));

        TextView textView = (TextView) findViewById(R.id.editGroup_nickTextView);
        textView.setText(group.getNameId());

        spinner = (Spinner) findViewById(R.id.editGroup_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarmLevelsArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(group.getGroupLevel() - 1);

        friendList = (ListView) findViewById(R.id.editGroup_friendListView);
        friendListAdapter = new DefaultFriendListAdapter(getApplicationContext(), R.layout.friend_list_item);

        friendListAdapter.setArrayList(group.getFriends());
        friendList.setAdapter(friendListAdapter);

        saveButton = (Button) findViewById(R.id.editGroup_saveButton);
        saveButton.setOnClickListener(this);

        inviteFriendsButton = (Button) findViewById(R.id.editGroup_inviteFriends);
        inviteFriendsButton.setOnClickListener(this);

        registerForContextMenu(friendList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editGroup_saveButton:
                group.setGroupLevel(spinner.getSelectedItemPosition() + 1);
                helper.updateGroup(group);
                onBackPressed();
                break;
            case R.id.editGroup_inviteFriends:
                Intent intent = new Intent(getApplicationContext(), InviteFriendsActivity.class);
                intent.putExtra(Constants.EXTRA_ID, group.getNameId());
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = new MenuInflater(getApplicationContext());
        menuInflater.inflate(R.menu.edit_group_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.editGroupMenu_delete:
                deleteFriend(info.position);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteFriend(int position) {
        Friend friend = friendListAdapter.getItem(position);

        //TODO: send request to server

        helper.deleteGroupFriend(group, friend);

        group = helper.getGroup(group.getNameId());
        friendListAdapter.setArrayList(group.getFriends());

        Toast.makeText(getApplicationContext(), "Member \"" + friend.getNick() + "\" deleted successfully", Toast.LENGTH_SHORT).show();
    }
}
