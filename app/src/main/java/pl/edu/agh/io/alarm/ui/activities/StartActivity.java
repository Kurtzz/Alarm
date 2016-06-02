package pl.edu.agh.io.alarm.ui.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.gcm.Constants;
import pl.edu.agh.io.alarm.gcm.id.InstanceRegistrationIntent;
import pl.edu.agh.io.alarm.middleware.Middleware;

/**
 * Created by Mateusz on 2016-06-02.
 */
public class StartActivity extends Activity implements View.OnClickListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = MainActivity.class.getSimpleName();

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

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
                    registerIdentityWithGoogle();
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


    private void registerIdentityWithGoogle() {
        Log.i(TAG, "Initializing token generation...");
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(Constants.TOKEN_REGISTERED, false);
                Log.i(TAG, "Received broadcast: tokenSent - " + sentToken);
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Log.i(TAG, "Starting ID intent...");
            Intent intent = new Intent(this, InstanceRegistrationIntent.class);
            startService(intent);
        }
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Constants.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
