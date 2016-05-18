package pl.agh.ki.io.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pl.agh.ki.io.alarm.alarm.R;
import pl.agh.ki.io.alarm.sqlite.helper.DatabaseHelper;
import pl.agh.ki.io.alarm.sqlite.model.Friend;

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHelper databaseHelper;
    private EditText editText;
    private static final int MAX_LEVEL = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        Button addFriendButton = (Button) findViewById(R.id.addFriend_addFriendButton);
        addFriendButton.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.addFriend_friendEditText);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        Friend friend = new Friend();
        friend.setNick(editText.getText().toString());
        friend.setLevel(MAX_LEVEL);
        editText.setText("");
        databaseHelper.createFriend(friend);

        Intent intent = new Intent(v.getContext(), MainActivity.class);
        startActivity(intent);
    }
}
