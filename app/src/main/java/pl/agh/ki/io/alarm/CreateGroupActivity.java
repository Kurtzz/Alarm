package pl.agh.ki.io.alarm;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import pl.agh.ki.io.alarm.alarm.R;
import pl.agh.ki.io.alarm.sqlite.helper.DatabaseHelper;
import pl.agh.ki.io.alarm.sqlite.model.Friend;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView friendList;
    private FriendListAdapter friendListAdapter;

    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        List<Friend> friends = databaseHelper.getFriends();

        friendList = (ListView) findViewById(R.id.createGroup_friendListView);
        friendListAdapter = new FriendListAdapter(getApplicationContext(), R.layout.friend_list_item_multi, friends);

        friendListAdapter.setArrayList(friends);
        friendList.setAdapter(friendListAdapter);
    }



    @Override
    public void onClick(View v) {

    }
}
