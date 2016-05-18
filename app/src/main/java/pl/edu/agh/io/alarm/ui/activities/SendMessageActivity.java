package pl.edu.agh.io.alarm.ui.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.notifications.Notifications;
import pl.edu.agh.io.alarm.sqlite.helper.DatabaseHelper;
import pl.edu.agh.io.alarm.sqlite.model.Friend;
import pl.edu.agh.io.alarm.ui.UI;

/**
 * Created by Mateusz on 2016-04-21.
 */
public class SendMessageActivity extends Activity implements View.OnClickListener {
    private UI ui;
    private String nickname;
    private ArrayList<String> friendList = new ArrayList<>();
    private boolean mIsBound;
    private Notifications notificationsService;
    private DatabaseHelper databaseHelper;


    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_sendmessage);
        Button send = (Button) findViewById(R.id.SENDMESSAGE_sendbtn);
        send.setOnClickListener(this);
        ImageButton imageButton = (ImageButton) findViewById(R.id.SENDMESSAGE_exitbtn);
        imageButton.setOnClickListener(this);
        doBindService();
        databaseHelper = new DatabaseHelper(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        friendList.clear();
        List<Friend> fList = databaseHelper.getFriends();
        for(Friend friend : fList){
            friendList.add(friend.getNick());
        }
        final ListView listView = (ListView) findViewById(R.id.SENDMESSAGE_FriendListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_view_content, friendList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nickname = listView.getItemAtPosition(position).toString();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.SENDMESSAGE_sendbtn:
                EditText editText = (EditText) findViewById(R.id.SENDMESSAGE_msgtext);
                if(!editText.getText().toString().isEmpty()){
                    notificationsService.makeNotification(nickname,editText.getText().toString());
                    this.finish();

                }
                break;
            case R.id.SENDMESSAGE_exitbtn:
                this.finish();
                break;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            notificationsService = ((Notifications.LocalBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            notificationsService = null;
           }
    };

    void doBindService() {
        bindService(new Intent(getApplicationContext(),
                Notifications.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
}
