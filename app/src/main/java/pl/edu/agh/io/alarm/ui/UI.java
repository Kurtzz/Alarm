package pl.edu.agh.io.alarm.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.gcm.Constants;
import pl.edu.agh.io.alarm.gcm.id.InstanceRegistrationIntent;
import pl.edu.agh.io.alarm.ui.activities.LoginActivity;

/**
 * Created by Mateusz on 2016-04-21.
 */
public class UI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
