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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ConcurrentLinkedDeque;

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
        final Friend friend = new Friend();
        String friendUuid = editText.getText().toString();
        if (friendUuid.isEmpty()) {
            Toast.makeText(this, "Uuid can't be blank!", Toast.LENGTH_SHORT).show();
            return;
        } else if (friendUuid.contains(" ") || friendUuid.contains("\t") || friendUuid.contains("\n")) {
            Toast.makeText(this, "Uuid can't contain white spaces!", Toast.LENGTH_SHORT).show();
            return;
        }

        friend.setId(editText.getText().toString());
        friend.setLevel(MAX_LEVEL);

        createToastStatusReceiver();
        createInvitationResultCallbackReceiver(friend, friendUuid);
        middleware.addUserAsFriend(friendUuid);

        editText.setText("");
        onBackPressed();
    }

    private void createInvitationResultCallbackReceiver(final Friend friend, String friendUuid) {
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String result = intent.getStringExtra(Constants.INVITATION_RESPONSE);
                String nick = intent.getStringExtra(Constants.NICKNAME);
                if(result.equals(Constants.INVITATION_ACCEPTED)) {
                    friend.setNick(nick);
                    Log.i("AddFriendAct", "Confirmed - Adding friend to database");
                    databaseHelper.createFriend(friend);
                    middleware.makeNotification("Invitation accepted", nick + " accepted your friend invitation");
                } else if (result.equals(Constants.INVITATION_DECLINED)) {
                    middleware.makeNotification("Invitation declined", nick + " rejected your friend invitation");
                }
            }
        }, new IntentFilter(Constants.INVITATION_PREFIX + friendUuid));
    }

    private void createToastStatusReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String status = intent.getStringExtra("status");
                Toast.makeText(AddFriendActivity.this, status, Toast.LENGTH_SHORT).show();
            }
        }, new IntentFilter(Constants.INVITATION_SENT));
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
