package pl.edu.agh.io.alarm.gcm;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GcmSendService extends Service {

    private static final String TAG = GcmSendService.class.getSimpleName();
    public static final String SEND_MESSAGE_URL = "http://www.jdabrowa.pl:8090/alarm/message/send/";

    private final IBinder binder = new GcmSendBinder();
    public class GcmSendBinder extends Binder {
        public GcmSendService getService() {return GcmSendService.this;}
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void sendToUser(String user, String message) {
        new SendGcmMessageAsync().execute("user", message);
    }

    public void sendToGroup(String message, String group) throws Exception {
        new SendGcmMessageAsync().execute("group", message);
    }

    class SendGcmMessageAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String ... params) {
            try {
                Log.i(TAG, "Sending message");

                String messageType = params[0];
                String sendMessageUrl = SEND_MESSAGE_URL + messageType;
                RestCommunication rest = new RestCommunication(sendMessageUrl);

                Map<String, String> requestParams = new HashMap<>();
//                requestParams.put("NICKNAME", nickname);
//                requestParams.put("")
//
//                Log.i(TAG, "Response code: " + responseCode);
//                Log.i(TAG, "Message sent");
                rest.execute(requestParams, "PUT");
            } catch (IOException e) {
                Log.w(TAG, "Cannot execute REST call", e);
            }
            return null;
        }
    }
}
