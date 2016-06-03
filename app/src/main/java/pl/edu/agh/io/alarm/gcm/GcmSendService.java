package pl.edu.agh.io.alarm.gcm;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.io.alarm.middleware.Middleware;
import pl.edu.agh.io.alarm.sqlite.model.User;

public class GcmSendService extends Service {

    private static final String TAG = GcmSendService.class.getSimpleName();
    public static final String SEND_MESSAGE_URL = "http://www.jdabrowa.pl:8090/alarm/message/send/";
    public static final String SEND_INVITATION_URL = "http://www.jdabrowa.pl:8090/alarm/friend/invite/";

    private final IBinder binder = new GcmSendBinder();

    public class GcmSendBinder extends Binder {
        public GcmSendService getService() {return GcmSendService.this;}
    }

    private Middleware middleware;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bindService(new Intent(getApplicationContext(), Middleware.class), middlewareConnection, Context.BIND_AUTO_CREATE);
    }

    public void sendToUser(String userId, String message, int level) {
        new SendGcmMessageAsync().execute("user", message, userId, String.valueOf(level));
    }

    public void sendToGroup(String group, String message, int level) throws Exception {
        new SendGcmMessageAsync().execute("group", message, group, String.valueOf(level));
    }

    public void addUserAsFriend(String nick) {
        new InvitationGcmAsync().execute(nick);
    }

    public void acceptInvitation(int invitationId) {
        new GcmInviteResponse().execute("accept", String.valueOf(invitationId));
    }

    public void declineInvitation(int invitationId) {
        new GcmInviteResponse().execute("decline", String.valueOf(invitationId));
    }

    class SendGcmMessageAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String ... params) {
            try {
                Log.i(TAG, "Sending message");

                User currentUser = middleware.getUser();

                String messageType = params[0];
                String sendMessageUrl = SEND_MESSAGE_URL + messageType;
                RestCommunication rest = new RestCommunication(sendMessageUrl);

                Map<String, String> requestParams = new HashMap<>();
                requestParams.put(Constants.NICKNAME, currentUser.getNickname());
                requestParams.put(Constants.SENDER_UID, currentUser.getUid());
                requestParams.put(Constants.MESSAGE, params[1]);
                requestParams.put(Constants.MESSAGE_RECEIVER, params[2]);
                requestParams.put(Constants.LEVEL, params[3]);

                RestCommunication.ConnectionResponse response = rest.execute(requestParams, "PUT");
                Log.i(TAG, "Response code: " + response.getStatus());
                Log.i(TAG, "Message sent");
            } catch (IOException e) {
                Log.w(TAG, "Cannot execute REST call", e);
            }
            return null;
        }
    }

    class InvitationGcmAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Log.i(TAG, "Sending invitation");

                User currentUser = middleware.getUser();

                String sendMessageUrl = SEND_INVITATION_URL;
                RestCommunication rest = new RestCommunication(sendMessageUrl);

                Map<String, String> requestParams = new HashMap<>();
                requestParams.put(Constants.TOKEN, currentUser.getToken());
                requestParams.put(Constants.SENDER_UID, currentUser.getUid());
                requestParams.put(Constants.INVITEE_UID, strings[0]);
                requestParams.put(Constants.GROUP_NAME, null);

                RestCommunication.ConnectionResponse response = rest.execute(requestParams, "POST");

                Log.i(TAG, "Response code: " + response.getStatus());
                String responseAsString = response.getResponseAsString();
                Log.i(TAG, "Response: " + responseAsString);
                Intent intent = new Intent(Constants.INVITATION_SENT);
                intent.putExtra("status", responseAsString);
                LocalBroadcastManager.getInstance(GcmSendService.this).sendBroadcast(intent);
                Log.i(TAG, "Message sent");
            } catch (IOException e) {
                Log.w(TAG, "Cannot execute REST call", e);
            }
            return null;
        }
    }

    class GcmInviteResponse extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String result = strings[0];
            Log.i(TAG, "Sending invitation response: " + result);
            RestCommunication rest = new RestCommunication("http://jdabrowa.pl:8090/alaram/invitation/" + result);
            Map<String, String> messageBody = new HashMap<>();
            messageBody.put("INVITATION_ID", strings[1]);
            try {
                rest.execute(messageBody, "POST");
            } catch (IOException e) {
                Log.w("Communication error", e);
            }

            return null;
        }
    }

    private final ServiceConnection middlewareConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            middleware = ((Middleware.LocalBinder)iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            middleware = null;
        }
    };
}
