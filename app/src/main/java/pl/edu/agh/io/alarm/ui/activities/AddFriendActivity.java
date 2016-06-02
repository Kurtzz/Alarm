package pl.edu.agh.io.alarm.ui.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.gcm.Constants;
import pl.edu.agh.io.alarm.middleware.Middleware;
import pl.edu.agh.io.alarm.sqlite.helper.DatabaseHelper;
import pl.edu.agh.io.alarm.sqlite.model.Friend;

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHelper databaseHelper;
    private EditText editText;
    private static final int MAX_LEVEL = 5;
    private Middleware middleware;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        //za kazdym razem nowy database helper

        Button addFriendButton = (Button) findViewById(R.id.addFriend_addFriendButton);
        addFriendButton.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.addFriend_friendEditText);

        bindService(new Intent(getApplicationContext(), Middleware.class), middlewareConnection, Context.BIND_AUTO_CREATE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        Friend friend = new Friend();
        String nick = editText.getText().toString();
        if (nick.isEmpty()) {
            Toast.makeText(this, "Nick can't be blank!", Toast.LENGTH_SHORT).show();
            return;
        } else if (nick.contains(" ") || nick.contains("\t") || nick.contains("\n")) {
            Toast.makeText(this, "Nick can't contain white spaces!", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String status = intent.getStringExtra("status");
                Toast.makeText(AddFriendActivity.this, status, Toast.LENGTH_SHORT).show();
            }
        }, new IntentFilter(Constants.INVITATION_SENT));

        middleware.addUserAsFriend(nick);

        // TODO: Should be added to databse after invitation has been accepted
        friend.setId(editText.getText().toString()); //TODO: set ID from Server DB
        friend.setNick(editText.getText().toString());
        friend.setLevel(MAX_LEVEL);
        editText.setText("");
        databaseHelper.createFriend(friend);
        onBackPressed();
    }

    private final ServiceConnection middlewareConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AddFriendActivity.this.middleware = ((Middleware.LocalBinder)iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            AddFriendActivity.this.middleware = null;
        }
    };
}
