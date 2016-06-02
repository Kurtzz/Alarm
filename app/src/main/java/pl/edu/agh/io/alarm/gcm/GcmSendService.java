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

public class GcmSendService extends Service {

    private static final String TAG = GcmSendService.class.getSimpleName();

    private final IBinder binder = new GcmSendBinder();
    public class GcmSendBinder extends Binder {
        public GcmSendService getService() {return GcmSendService.this;}
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void sendTo(String user, String message) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void sendToAll(String message) throws Exception {
        new SendGcmMessageAsync().execute(message);
    }

    class SendGcmMessageAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String ... messages) {
            try {
                Log.i(TAG, "Sending message");
                URL serverUrl = new URL("http://www.jdabrowa.pl:8090/alarm/message/send/" + messages[0]);
                HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
                connection.setRequestMethod("PUT");

                int responseCode = connection.getResponseCode();
                connection.getInputStream();
                Log.i(TAG, "Response code: " + responseCode);
                Log.i(TAG, "Message sent");
            } catch (IOException e) {
                Log.w(TAG, "Cannot execute REST call", e);
            }
            return null;
        }
    }
}
