package pl.agh.ki.io.alarm;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pl.agh.ki.io.alarm.alarm.R;
import pl.agh.ki.io.alarm.sqlite.helper.DatabaseHelper;
import pl.agh.ki.io.alarm.sqlite.model.Friend;
import pl.agh.ki.io.alarm.sqlite.model.Group;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView friendList;
    private MultiChoiceFriendListAdapter friendListAdapter;
    private Button createGroupButton;
    private EditText nameEditText;

    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        nameEditText = (EditText) findViewById(R.id.createGroup_nameEditText);

        createGroupButton = (Button) findViewById(R.id.createGroup_createGroupButton);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
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

                Group group = new Group();
                group.setGroupName(nameEditText.getText().toString());
                group.setGroupLevel(5);
                group.setFriends(checkedFriends);
                databaseHelper.createGroup(group);

                nameEditText.setText("");
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        List<Friend> friends = databaseHelper.getFriends();

        friendList = (ListView) findViewById(R.id.createGroup_friendListView);
        friendListAdapter = new MultiChoiceFriendListAdapter(getApplicationContext(), R.layout.friend_list_item_multi, friends);

        friendListAdapter.setArrayList(friends);
        friendList.setAdapter(friendListAdapter);
    }



    @Override
    public void onClick(View v) {

    }
}
