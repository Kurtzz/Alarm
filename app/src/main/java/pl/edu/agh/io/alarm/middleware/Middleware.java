package pl.edu.agh.io.alarm.middleware;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

import pl.edu.agh.io.alarm.gcm.GcmSendService;
import pl.edu.agh.io.alarm.notifications.Notifications;
import pl.edu.agh.io.alarm.sqlite.model.Friend;
import pl.edu.agh.io.alarm.sqlite.model.Group;
import pl.edu.agh.io.alarm.sqlite.model.User;
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

    public void makeNotification(String title,String text){
        notificationService.makeNotification(title,text);
    }
    public void makeInvite(String nickname, String groupName){
        notificationService.makeInvite(nickname,groupName);
    }

    public void makeAlarm(String nickname, String text, int level){
        notificationService.makeAlarm(nickname,text,level);
    }

    public void makeAlarm(String nickname, String text){
        notificationService.makeAlarm(nickname,text);
    }

    public void sendMessageToGroup(String message, String group, int level) {
        try {
            messagingService.sendToGroup(group, message, level);
        } catch (Exception e) {
            Log.i(TAG, "Sending error", e);
        }
    }

    public void sendMessageToUser(String message, String userId, int level) {
        try {
            messagingService.sendToGroup(userId, message, level);
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

    public User getUser() {
        return databaseService.getUser();
    }

    public void createUser(User user) {
        databaseService.createUser(user);
    }


    public void addUserAsFriend(String nick) {
        messagingService.addUserAsFriend(nick);
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

        Intent gcmBindIntent = new Intent(getApplicationContext(), GcmSendService.class);
        bindService(gcmBindIntent, gcmConnection, Context.BIND_AUTO_CREATE);
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

    private static MediaPlayer mediaPlayer = null;
    public static MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }
    public static void setMediaPlayer(MediaPlayer p){
        mediaPlayer  = p;
    }


}
