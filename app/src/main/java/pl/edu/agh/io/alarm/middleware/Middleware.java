package pl.edu.agh.io.alarm.middleware;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import java.util.List;

import pl.edu.agh.io.alarm.notifications.Notifications;
import pl.edu.agh.io.alarm.sqlite.model.Friend;
import pl.edu.agh.io.alarm.sqlite.model.Group;
import pl.edu.agh.io.alarm.sqlite.service.DatabaseService;

/**
 * Created by Mateusz on 2016-05-18.
 */
public class Middleware extends Service {

    private final IBinder mBinder = new LocalBinder();
    private DatabaseService databaseService;
    private boolean databaseIsBound;
    private boolean notificationIsBound;
    private Notifications notificationService;


    public class LocalBinder extends Binder {
        public Middleware getService() {
            return Middleware.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("ONBIND !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        doBindService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }


    public void makeNotification(String nickname, String text){
        notificationService.makeNotification(nickname,text);
    }

    public long createFriend(Friend friend) {
        return databaseService.createFriend(friend);
    }

    public Friend getFriend(long friend_id) {
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
    public void deleteFriend(long friend_id) {
        databaseService.deleteFriend(friend_id);
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
    public Group getGroup(long group_id) {
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
    public void deleteGroup(long group_id) {
        databaseService.deleteGroup(group_id);
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

    void doBindService() {
        bindService(new Intent(getApplicationContext(),
                DatabaseService.class), databaseConnection, Context.BIND_AUTO_CREATE);
        databaseIsBound = true;

        bindService(new Intent(getApplicationContext(),
                Notifications.class), notificationConnection, Context.BIND_AUTO_CREATE);
        notificationIsBound = true;

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
    }


}
