package pl.agh.ki.io.alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import pl.agh.ki.io.alarm.alarm.R;
import pl.agh.ki.io.alarm.sqlite.helper.DatabaseHelper;
import pl.agh.ki.io.alarm.sqlite.model.Friend;
import pl.agh.ki.io.alarm.sqlite.model.Group;

public class EditGroupActivity extends AppCompatActivity {
    private Spinner spinner;
    private Group group;
    private Button button;
    private ListView friendList;
    private MultiChoiceFriendListAdapter friendListAdapter;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        group = databaseHelper.getGroup(getIntent().getIntExtra(SendMessageActivity.EXTRA_ID, 0));

        TextView textView = (TextView) findViewById(R.id.editGroup_nickTextView);
        textView.setText(group.getGroupName());

        spinner = (Spinner) findViewById(R.id.editGroup_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarmLevelsArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(group.getGroupLevel() - 1);

        List<Friend> friends = databaseHelper.getFriends();

        friendList = (ListView) findViewById(R.id.editGroup_friendListView);
        friendListAdapter = new MultiChoiceFriendListAdapter(getApplicationContext(), R.layout.friend_list_item_multi);

        friendListAdapter.setArrayList(friends);
        friendList.setAdapter(friendListAdapter);

        button = (Button) findViewById(R.id.editGroup_saveButton);
    }
}
