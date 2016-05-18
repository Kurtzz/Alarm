package pl.edu.agh.io.alarm.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import pl.edu.agh.io.alarm.R;

/**
 * Created by Mateusz on 2016-05-18.
 */
public class MainActivity extends Activity implements  View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Main Activity Created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton sendMsg = (ImageButton) findViewById(R.id.ImageButton_SendMessage);
        ImageButton addFriend  = (ImageButton) findViewById(R.id.ImageButton_AddFriend);
        ImageButton createGroup = (ImageButton) findViewById(R.id.ImageButton_CreateGroup);

        sendMsg.setOnClickListener(this);
        addFriend.setOnClickListener(this);
        createGroup.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ImageButton_AddFriend:
                Intent intent = new Intent(this,AddFriendActivity.class);
                startActivity(intent);
                break;
//
//            case R.id.ImageButton_CreateGroup:
//                Intent intent2 = new Intent(this,CreateGroupActivity.class);
//                startActivity(intent2);
//                break;

            case R.id.ImageButton_SendMessage:
                Intent intent3 = new Intent(this,SendMessageActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
