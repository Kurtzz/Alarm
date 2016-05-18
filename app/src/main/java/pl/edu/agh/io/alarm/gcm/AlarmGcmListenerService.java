package pl.edu.agh.io.alarm.gcm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import pl.edu.agh.io.alarm.notifications.Notifications;

public class AlarmGcmListenerService extends GcmListenerService{

    private static String TAG = AlarmGcmListenerService.class.getSimpleName();

    private Notifications notificationsService;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Binding to service...");
        bindService(new Intent(getApplicationContext(),
                Notifications.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        while(notificationsService == null) {}
        notificationsService.makeNotification("Message!", message);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i(TAG, "Service connected");
            notificationsService = ((Notifications.LocalBinder)service).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName className) {
            notificationsService = null;
        }
    };
}
