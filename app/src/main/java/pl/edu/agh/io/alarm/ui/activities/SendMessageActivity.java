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
import android.widget.Toast;

import java.util.ArrayList;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.notifications.Notifications;
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


    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_sendmessage);
        Button send = (Button) findViewById(R.id.SENDMESSAGE_sendbtn);
        send.setOnClickListener(this);
        ImageButton imageButton = (ImageButton) findViewById(R.id.SENDMESSAGE_exitbtn);
        imageButton.setOnClickListener(this);
        doBindService();

    }

    @Override
    protected void onResume() {
        super.onResume();
        friendList.add("resume");
        Bundle extras = getIntent().getExtras();
        if (extras!= null){
            String value = extras.getString("Friends_list");
            friendList.clear();
            friendList.add(value);

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
                    //TODO: Send to Communicate Module Friend and Text, then change to MainPageActivity
                    //TODO: wysylanie nicku i tekstu do modulu
//                    Intent sendMsg = new Intent(this,CommunicationModule.class);
//                    sendMsg.setAction("SENDMSG");
//                    sendMsg.putExtra("NICKNAME",nickname);
//                    sendMsg.putExtra("Text",editText.getText().toString());
//                    startActivity(sendMsg);
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
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            notificationsService = ((Notifications.LocalBinder)service).getService();

            // Tell the user about this for our demo.
            Toast.makeText(getApplicationContext(), "Connected",
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            notificationsService = null;
            Toast.makeText(getApplicationContext(), "Disconnected",
                    Toast.LENGTH_SHORT).show();
        }
    };

    void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(getApplicationContext(),
                Notifications.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
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
