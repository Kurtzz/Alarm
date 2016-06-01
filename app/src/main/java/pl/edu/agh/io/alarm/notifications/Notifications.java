package pl.edu.agh.io.alarm.notifications;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import pl.edu.agh.io.alarm.R;

public class Notifications extends IntentService {
    private NotificationManager mNM;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public Notifications(String name) {
        super(name);
    }
    public Notifications(){
        super("Notifications Service");
    }

    public void makeNotification(){
        makeNotification("testNickname","testText");
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void makeNotification(String nickname, String text) {

        // The PendingIntent to launch our activity if the user selects this notification
        Intent myClassIntent = new Intent(getApplicationContext(), MyClass.class);
        myClassIntent.putExtra("nickname",nickname );
        myClassIntent.putExtra("text",text);

        System.out.println("NOTIFICATION CONTEXT:   "+getApplicationContext().toString());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                myClassIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(nickname)  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setAutoCancel(true)
                .build();

        // Send the notification.
        mNM.notify(NOTIFICATION++, notification);

    }


    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        public Notifications getService() {
            return Notifications.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Notifications", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, android.R.string.VideoView_error_text_unknown, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("I get An Intent");
//        String string = intent.getStringExtra("test");
//        if(string!= null){
//            Toast.makeText(this, string,Toast.LENGTH_SHORT).show();
//        }
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

 }