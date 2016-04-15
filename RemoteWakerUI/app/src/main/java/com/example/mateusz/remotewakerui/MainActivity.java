package com.example.mateusz.remotewakerui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public void addFriendLayout(View view) {
        setContentView(R.layout.activity_addfriend);
    }

    public void createGroupLayout(View view) {
        setContentView(R.layout.activity_creategroup);
    }

    public void sendMessageLayout(View view) {
        setContentView(R.layout.activity_sendmessage);
    }

    public void loginLayout(View view) {
        setContentView(R.layout.activity_login);
    }

    public void mainActivityLayoutAndSendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.SENDMESSAGE_msgtext);
        if(editText != null) {
            String msg = editText.getText().toString();
            System.out.println(msg);
        }
        setContentView(R.layout.activity_main);
    }
    public void mainActivityLayout(View view) {
        setContentView(R.layout.activity_main);
    }
}
