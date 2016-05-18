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
import pl.edu.agh.io.alarm.middleware.Middleware;
import pl.edu.agh.io.alarm.sqlite.model.Friend;

/**
 * Created by Mateusz on 2016-04-21.
 */
public class SendMessageActivity extends Activity implements View.OnClickListener {
    private String nickname;
    private ArrayList<String> friendList = new ArrayList<>();
    private boolean middlewareIsBound;
    private Middleware middlewareService;


    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        doBindService();

        setContentView(R.layout.activity_sendmessage);
        Button send = (Button) findViewById(R.id.SENDMESSAGE_sendbtn);
        send.setOnClickListener(this);
        ImageButton imageButton = (ImageButton) findViewById(R.id.SENDMESSAGE_exitbtn);
        imageButton.setOnClickListener(this);
        Button refresh  = (Button) findViewById(R.id.SENDMESSAGE_refresh);
        refresh.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();


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
//                    middlewareService.makeNotification(nickname,editText.getText().toString());
                      middlewareService.sendMessageToAll(editText.getText().toString());
                    this.finish();

                }
                break;
            case R.id.SENDMESSAGE_exitbtn:
                this.finish();
                break;
            case R.id.SENDMESSAGE_refresh:
                friendList.clear();
                if(middlewareService != null) {
                    List<Friend> fList = middlewareService.getFriends();

                    for (Friend friend : fList) {
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


                break;
        }
    }
    private ServiceConnection middlewareConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            middlewareService = ((Middleware.LocalBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            middlewareService = null;
        }
    };



    void doBindService() {
        System.out.println("BOUNDING");
        bindService(new Intent(getApplicationContext(),
                Middleware.class), middlewareConnection, Context.BIND_AUTO_CREATE);
        middlewareIsBound = true;
    }

    void doUnbindService() {
        if (middlewareIsBound) {
            unbindService(middlewareConnection);
            middlewareIsBound = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
}
