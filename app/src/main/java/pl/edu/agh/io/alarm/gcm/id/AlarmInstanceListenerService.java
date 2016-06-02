package pl.edu.agh.io.alarm.gcm.id;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

public class AlarmInstanceListenerService extends InstanceIDListenerService {

    private static final String TAG = "InstanceListenerService";

    @Override
    public void onTokenRefresh() {
        Log.w(TAG, "onTokenRefresh");
        Intent intent = new Intent(this, InstanceRegistrationIntent.class);
        startService(intent);
    }
}
