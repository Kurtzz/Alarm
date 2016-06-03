package pl.edu.agh.io.alarm.gcm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import pl.edu.agh.io.alarm.middleware.Middleware;

public class AlarmGcmListenerService extends GcmListenerService{

    private static String TAG = AlarmGcmListenerService.class.getSimpleName();

    private Middleware middlewareService;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Binding to service...");
        bindService(new Intent(getApplicationContext(),
                Middleware.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        while(middlewareService == null) {}

        String messageType = data.getString("messageType");

        String invitationResponse = data.getString("invitationResponse");
        String nick = data.getString("senderNick");
        String senderUuid = data.getString("senderUID");

        switch (messageType) {
            case "INVITATION_RESPONSE":
                Intent intent = new Intent(Constants.INVITATION_PREFIX + senderUuid);
                intent.putExtra(Constants.INVITATION_RESPONSE, invitationResponse);
                intent.putExtra(Constants.NICKNAME, nick);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                Log.i(TAG, "Invitation response: " + invitationResponse);
                break;
            case "INVITATION":
                String groupName = data.getString("GROUP_ID");
                if(groupName == null) groupName = "friend";
                int invitationId = data.getInt("invitationId");
                middlewareService.makeInvite(nick, groupName, invitationId);
                break;

            case "MESSAGE":
                middlewareService.makeAlarm(nick, data.getString("message"));
        }

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i(TAG, "Service connected");
            middlewareService = ((Middleware.LocalBinder)service).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName className) {
            middlewareService = null;
        }
    };
}
