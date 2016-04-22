package pl.edu.agh.io.alarm.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.ui.UI;

/**
 * Created by Mateusz on 2016-04-21.
 */
public class SendMessageActivity extends Activity implements View.OnClickListener {
    private UI ui;


    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_sendmessage);
        Button send = (Button) findViewById(R.id.SENDMESSAGE_sendbtn);
        send.setOnClickListener(this);
        ImageButton imageButton = (ImageButton) findViewById(R.id.SENDMESSAGE_exitbtn);
        imageButton.setOnClickListener(this);
        System.out.println("Created");
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
                if(editText.getText() != null){
                    //TODO: Send to Communicate Module Friend and Text, then change to MainPageActivity
                    System.out.println(editText.getText().toString());
                    Intent intent = new Intent(this, AddFriendActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.SENDMESSAGE_exitbtn:
                System.out.println("ExitBtn Clicked");
                break;
        }
    }
}
