package pl.edu.agh.io.alarm.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.sqlite.helper.DatabaseHelper;
import pl.edu.agh.io.alarm.sqlite.model.Friend;
import pl.edu.agh.io.alarm.sqlite.model.Group;
import pl.edu.agh.io.alarm.ui.adapters.MultiChoiceFriendListAdapter;

public class InviteFriendsActivity extends AppCompatActivity implements View.OnClickListener {
    private Group group;
    private Button button;
    private ListView friendList;
    private MultiChoiceFriendListAdapter friendListAdapter;

    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        helper = new DatabaseHelper(getApplicationContext());
        group = helper.getGroup(getIntent().getIntExtra(SendMessageActivity.EXTRA_ID, 0));

        TextView textView = (TextView) findViewById(R.id.inviteFriends_nickTextView);
        textView.setText(group.getGroupName());

        List<Friend> friends = helper.getFriends();
        friends.removeAll(group.getFriends());

        friendList = (ListView) findViewById(R.id.inviteFriends_friendListView);
        friendListAdapter = new MultiChoiceFriendListAdapter(getApplicationContext());

        friendListAdapter.setArrayList(friends);
        friendList.setAdapter(friendListAdapter);

        button = (Button) findViewById(R.id.inviteFriends_inviteButton);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SparseBooleanArray checkedItems = friendListAdapter.getCheckedItems();
        List<Friend> checkedFriends = new ArrayList<>();

        for (int i = 0; i < checkedItems.size(); i++) {
            int key = checkedItems.keyAt(i);
            if (checkedItems.valueAt(i)) {
                checkedFriends.add(friendListAdapter.getItem(key));
            }
        }

        //TODO: invite checkedFriends

        onBackPressed();
    }
}
