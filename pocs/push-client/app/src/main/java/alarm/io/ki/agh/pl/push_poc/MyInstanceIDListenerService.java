package alarm.io.ki.agh.pl.push_poc;

import android.util.Log;

import com.google.android.gms.iid.InstanceID;
import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService {

    public MyInstanceIDListenerService() {
        super();
        Log.i(this.getClass().getSimpleName(), "Before getting instance id");
        String iid = InstanceID.getInstance(this).getId();
        Log.i(this.getClass().getSimpleName(), "Received instance id: " + iid);
    }

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
