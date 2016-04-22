package pl.edu.agh.io.alarm.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pl.edu.agh.io.alarm.R;

/**
 * Created by Mateusz on 2016-04-22.
 */
public class LoginActivity extends Activity implements View.OnClickListener {



    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_login);
        Button send = (Button) findViewById(R.id.LOGINLoginbtn);
        send.setOnClickListener(this);
        System.out.println("LoginActivity : Create");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LOGINLoginbtn:
                EditText loginText = (EditText) findViewById(R.id.LOGINloginText);
                EditText passwordText = (EditText) findViewById(R.id.LOGINpasswordText);
                if(loginText != null && passwordText != null){
                    System.out.println("Login: "+loginText.getText().toString());
                    System.out.println("Login: " + passwordText.getText().toString());
                    Intent intent = new Intent(this, SendMessageActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

}
