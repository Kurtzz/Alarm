package pl.edu.agh.io.alarm.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.sqlite.helper.DatabaseHelper;
import pl.edu.agh.io.alarm.sqlite.model.Friend;
import pl.edu.agh.io.alarm.sqlite.model.Group;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private Button createGroupButton;
    private Button joinGroupButton;
    private EditText nameEditText;

    private DatabaseHelper helper;

    private static final int MAX_LEVEL = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        helper = new DatabaseHelper(getApplicationContext());

        nameEditText = (EditText) findViewById(R.id.createGroup_nameEditText);

        createGroupButton = (Button) findViewById(R.id.createGroup_createGroupButton);
        createGroupButton.setOnClickListener(this);

        joinGroupButton = (Button) findViewById(R.id.createGroup_joinGroupButton);
        joinGroupButton.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        String groupName = nameEditText.getText().toString();
        if (groupName.isEmpty()) {
            Toast.makeText(this, "Group's name can't be blank!", Toast.LENGTH_SHORT).show();
            return;
        } else if (groupName.contains("\t") || groupName.contains("\n")) {
            Toast.makeText(this, "Group's name can't contains white spaces!", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {
            case R.id.createGroup_joinGroupButton:
                //TODO: server staff
                break;
            case R.id.createGroup_createGroupButton:

                //TODO: send request to server

                Group group = new Group();
                group.setNameId(nameEditText.getText().toString()); //TODO: set ID from Server
                group.setGroupLevel(MAX_LEVEL);
                group.setFriends(new ArrayList<Friend>());
                helper.createGroup(group);

                //TODO: Send invitation to checkedFriends

                Intent intent = new Intent(getApplicationContext(), InviteFriendsActivity.class);
                intent.putExtra(SendMessageActivity.EXTRA_ID, group.getNameId());
                startActivity(intent);

                onBackPressed();
                break;
        }

    }
}
