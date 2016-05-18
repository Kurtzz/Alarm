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
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.gcm.Constants;
import pl.edu.agh.io.alarm.gcm.id.InstanceRegistrationIntent;

/**
 * Created by Mateusz on 2016-05-18.
 */
public class MainActivity extends Activity implements  View.OnClickListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = MainActivity.class.getSimpleName();

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

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
        registerIdentityWithGoogle();

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
