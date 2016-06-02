package pl.edu.agh.io.alarm.middleware;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

import pl.edu.agh.io.alarm.gcm.GcmSendService;
import pl.edu.agh.io.alarm.notifications.Notifications;
import pl.edu.agh.io.alarm.sqlite.model.Friend;
import pl.edu.agh.io.alarm.sqlite.model.Group;
import pl.edu.agh.io.alarm.sqlite.service.DatabaseService;

public class    Middleware extends Service {

    private static final String TAG = Middleware.class.getSimpleName();

    private final IBinder mBinder = new LocalBinder();
    private DatabaseService databaseService;
    private boolean databaseIsBound;
    private boolean notificationIsBound;
    private boolean gcmIsBound;
    private Notifications notificationService;
    private GcmSendService messagingService;
    private String nickname;

    public class LocalBinder extends Binder {
        public Middleware getService() {
            return Middleware.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        doBindServices();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void makeNotification(String nickname, String text){
        notificationService.makeNotification(nickname,text);
    }

    public void sendMessageToGroup(String message, String group) {
        try {
            messagingService.sendToGroup(message, group);
        } catch (Exception e) {
            Log.i(TAG, "Sending error", e);
        }
    }

    public long createFriend(Friend friend) {
        return databaseService.createFriend(friend);
    }

    public Friend getFriend(String friend_id) {
        return databaseService.getFriend(friend_id);
    }

    /**
     * Get all friends
     */
    public List<Friend> getFriends() {
        return databaseService.getFriends();
    }

    /**
     * Delete friend
     */
    public void deleteFriend(Friend friend) {
        databaseService.deleteFriend(friend);
    }

    // ------------------------ "friends" table methods ----------------//

    /**
     * Create group
     */
    public long createGroup(Group group) {
        return databaseService.createGroup(group);
    }

    /**
     * Get single group
     */
    public Group getGroup(String group_id) {
        return databaseService.getGroup(group_id);
    }

    /**
     * Get all groups
     */
    public List<Group> getGroups() {
        return databaseService.getGroups();
    }

    /**
     * Delete Group
     */
    public void deleteGroup(Group group) {
        databaseService.deleteGroup(group);
    }





















    private ServiceConnection databaseConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            databaseService = ((DatabaseService.LocalBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            databaseService = null;
        }
    };


    private ServiceConnection notificationConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            notificationService = ((Notifications.LocalBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            databaseService = null;
        }
    };

    private ServiceConnection gcmConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messagingService = ((GcmSendService.GcmSendBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messagingService = null;
        }
    };

    void doBindServices() {
        bindService(new Intent(getApplicationContext(),
                DatabaseService.class), databaseConnection, Context.BIND_AUTO_CREATE);
        databaseIsBound = true;

        bindService(new Intent(getApplicationContext(),
                Notifications.class), notificationConnection, Context.BIND_AUTO_CREATE);
        notificationIsBound = true;

        bindService(new Intent(getApplicationContext(), GcmSendService.class),
                gcmConnection, Context.BIND_AUTO_CREATE);
        gcmIsBound = true;
    }

    void doUnbindService() {
        if (databaseIsBound) {
            unbindService(databaseConnection);
            databaseIsBound = false;
        }
        if (notificationIsBound) {
            unbindService(notificationConnection);
            notificationIsBound = false;
        }
        if(gcmIsBound) {
            unbindService(gcmConnection);
            gcmIsBound = false;
        }
    }


}
