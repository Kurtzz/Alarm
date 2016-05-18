package pl.edu.agh.io.alarm.ui.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.gcm.Constants;
import pl.edu.agh.io.alarm.gcm.id.InstanceRegistrationIntent;

/**
 * Created by Mateusz on 2016-04-22.
 */
public class LoginActivity extends Activity implements View.OnClickListener {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    System.out.println("Password: " + passwordText.getText().toString());
                    //TODO: Send to login module to validate login and password, then wait for intent from Login Module then start MainPageActivity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }




}
