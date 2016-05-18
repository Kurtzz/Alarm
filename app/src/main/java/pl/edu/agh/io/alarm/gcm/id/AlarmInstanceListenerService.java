package pl.edu.agh.io.alarm.gcm.id;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class AlarmInstanceListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, InstanceRegistrationIntent.class);
        startService(intent);
    }
}
