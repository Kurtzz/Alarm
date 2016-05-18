package pl.edu.agh.io.alarm.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.ui.UI;

/**
 * Created by Mateusz on 2016-04-21.
 */
public class SendMessageActivity extends Activity implements View.OnClickListener {
    private UI ui;
    private String nickname;
    private ArrayList<String> friendList = new ArrayList<>();


    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_sendmessage);
        Button send = (Button) findViewById(R.id.SENDMESSAGE_sendbtn);
        send.setOnClickListener(this);
        ImageButton imageButton = (ImageButton) findViewById(R.id.SENDMESSAGE_exitbtn);
        imageButton.setOnClickListener(this);

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
                    this.finish();

                }
                break;
            case R.id.SENDMESSAGE_exitbtn:
                this.finish();
                break;
        }
    }
}
