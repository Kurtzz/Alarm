package pl.edu.agh.io.alarm.gcm.id;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.io.alarm.gcm.Constants;
import pl.edu.agh.io.alarm.gcm.RestCommunication;

public class InstanceRegistrationIntent extends IntentService {

    private static final String TAG = InstanceRegistrationIntent.class.getSimpleName();

    private static final String PROJECT_ID = "1001998105077";
    private static final String SCOPE = "GCM";
    public static final String ADD_TOKEN_URL = "http://www.jdabrowa.pl:8090/alarm/tokens/add";

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

            String nickname = intent.getStringExtra(Constants.NICKNAME);

            sendRegistrationToServerObtainUuid(nickname, token);

            sharedPreferences.edit().putBoolean(Constants.TOKEN_REGISTERED, true).apply();
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(Constants.TOKEN_REGISTERED, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private String sendRegistrationToServerObtainUuid(String nickname, String token) throws IOException {

        Log.i(TAG, "Registering token");
        RestCommunication rest = new RestCommunication(ADD_TOKEN_URL);

        Map<String, String> body = new HashMap<>();
        body.put("TOKEN", token);
        body.put("NICKNAME", nickname);
        RestCommunication.ConnectionResponse response = rest.execute(body, "PUT");

        int responseCode = response.getStatus();

        Log.i(TAG, "Response code: " + responseCode);
        String uuid = response.getResponseAsString();
        Log.i(TAG, "Response: " + uuid);
        Log.i(TAG, "Token registered in server");
        return uuid;
    }
}
