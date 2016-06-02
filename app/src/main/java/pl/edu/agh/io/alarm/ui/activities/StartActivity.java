package pl.edu.agh.io.alarm.ui.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.middleware.Middleware;

/**
 * Created by Mateusz on 2016-06-02.
 */
public class StartActivity extends Activity implements View.OnClickListener {


    private boolean middlewareIsBound;
    private Middleware middlewareService;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_login);
        Button send = (Button) findViewById(R.id.LOGINLoginbtn);
        send.setOnClickListener(this);
        System.out.println("LoginActivity : Create");
        doBindServices();
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
                if(!loginText.getText().toString().equals("")){
                    middlewareService.setNickname(loginText.getText().toString());
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    private ServiceConnection middlewareConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            middlewareService = ((Middleware.LocalBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            middlewareService = null;
        }
    };




    void doBindServices() {
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

}
