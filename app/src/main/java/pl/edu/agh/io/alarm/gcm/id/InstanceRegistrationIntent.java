package pl.edu.agh.io.alarm.gcm.id;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.edu.agh.io.alarm.gcm.Constants;

public class InstanceRegistrationIntent extends IntentService {

    private static final String TAG = InstanceRegistrationIntent.class.getSimpleName();

    private static final String PROJECT_ID = "1001998105077";
    private static final String SCOPE = "GCM";

    public InstanceRegistrationIntent() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            Log.i(TAG, "Registering token...");
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(PROJECT_ID, SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);

            sendRegistrationToServer(token);
//            subscribeTopics(token);

            sharedPreferences.edit().putBoolean(Constants.TOKEN_REGISTERED, true).apply();
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(Constants.TOKEN_REGISTERED, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token) throws IOException {
        Log.i(TAG, "Registering token");
        URL serverUrl = new URL("http://www.jdabrowa.pl/alarm/register");
        HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoInput(true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(token);
        writer.close();
        int responseCode = connection.getResponseCode();
        connection.getInputStream();
        Log.i(TAG, "Response code: " + responseCode);
        Log.i(TAG, "Token registered in server");
    }
}
