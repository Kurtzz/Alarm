package pl.agh.ki.io.alarm.ui.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.agh.ki.io.alarm.alarm.R;
import pl.agh.ki.io.alarm.sqlite.helper.DatabaseHelper;
import pl.agh.ki.io.alarm.sqlite.model.Friend;
import pl.agh.ki.io.alarm.sqlite.model.Group;
import pl.agh.ki.io.alarm.ui.adapters.MultiChoiceFriendListAdapter;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView friendList;
    private MultiChoiceFriendListAdapter friendListAdapter;
    private Button createGroupButton;
    private EditText nameEditText;

    private DatabaseHelper databaseHelper;

    private static final int MAX_LEVEL = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        nameEditText = (EditText) findViewById(R.id.createGroup_nameEditText);

        createGroupButton = (Button) findViewById(R.id.createGroup_createGroupButton);
        createGroupButton.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        List<Friend> friends = databaseHelper.getFriends();

        friendList = (ListView) findViewById(R.id.createGroup_friendListView);
        friendListAdapter = new MultiChoiceFriendListAdapter(getApplicationContext());

        friendListAdapter.setArrayList(friends);
        friendList.setAdapter(friendListAdapter);
    }

    @Override
    public void onClick(View v) {
        String groupName = nameEditText.getText().toString();
        if (groupName.isEmpty()) {
            Toast.makeText(this, "Group's name can't be blank!", Toast.LENGTH_SHORT).show();
            return;
        } else if (groupName.contains(" ") || groupName.contains("\t") || groupName.contains("\n")) {
            Toast.makeText(this, "Group's name can't contains white spaces!", Toast.LENGTH_SHORT).show();
            return;
        }
        SparseBooleanArray checkedItems = friendListAdapter.getCheckedItems();
        List<Friend> checkedFriends = new ArrayList<>();

        for (int i = 0; i < checkedItems.size(); i++) {
            int key = checkedItems.keyAt(i);
            if (checkedItems.valueAt(i)) {
                checkedFriends.add(friendListAdapter.getItem(key));
            }
        }

        if (checkedFriends.size() < 2) {
            Toast.makeText(this, "Group should consist of at least 2 members!", Toast.LENGTH_SHORT).show();
            return;
        }

        Group group = new Group();
        group.setGroupName(nameEditText.getText().toString());
        group.setGroupLevel(MAX_LEVEL);
        group.setFriends(checkedFriends);
        databaseHelper.createGroup(group);

        nameEditText.setText("");
        onBackPressed();
    }
}