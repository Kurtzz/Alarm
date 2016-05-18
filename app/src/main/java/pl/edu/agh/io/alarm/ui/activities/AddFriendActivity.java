package pl.edu.agh.io.alarm.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import pl.edu.agh.io.alarm.R;

/**
 * Created by Mateusz on 2016-04-21.
 */
public class AddFriendActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("AddFriendActivity : Create");
        setContentView(R.layout.activity_addfriend);
        Button btn = (Button) findViewById(R.id.ADDFIREND_addbtn);
        btn.setOnClickListener(this);
        ImageButton b = (ImageButton) findViewById(R.id.ADDFRIEND_exitbtn);
        b.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("AddFriendActivity : Start");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("AddFriendActivity : Restart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("AddFriendActivity : Resume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("AddFriendActivity : Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("AddFriendActivity : Destory");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ADDFIREND_addbtn:
                EditText editText = (EditText) findViewById(R.id.ADDFRIEND_friend_name);
                if(editText.getText() != null){
                    //TODO: Send intent to Comunnicate Module, then wait for Respone;
                    System.out.println("AddFriendActivity : Friend's Name: "+editText.getText().toString());
                    Intent intent = new Intent(this,SendMessageActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ADDFRIEND_exitbtn:
                System.out.println("AddFriendActivity : ExitBtn Clicked");
                break;
        }
    }
}