package pl.agh.ki.io.alarm.sqlite.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.List;

import pl.agh.ki.io.alarm.sqlite.helper.DatabaseHelper;
import pl.agh.ki.io.alarm.sqlite.model.Friend;
import pl.agh.ki.io.alarm.sqlite.model.Group;

public class DatabaseService extends Service {


    private DatabaseHelper helper;
    private final IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        public DatabaseService getService() {
            return DatabaseService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        helper = new DatabaseHelper(getApplicationContext());
    }



    public long createFriend(Friend friend) {
        return helper.createFriend(friend);
    }

    public Friend getFriend(long friend_id) {
        return helper.getFriend(friend_id);
    }

    /**
     * Get all friends
     */
    public List<Friend> getFriends() {
        return helper.getFriends();
    }

    /**
     * Delete friend
     */
    public void deleteFriend(long friend_id) {
        helper.deleteFriend(friend_id);
    }

    // ------------------------ "friends" table methods ----------------//

    /**
     * Create group
     */
    public long createGroup(Group group) {
       return helper.createGroup(group);
    }

    /**
     * Get single group
     */
    public Group getGroup(long group_id) {
        return helper.getGroup(group_id);
    }

    /**
     * Get all groups
     */
    public List<Group> getGroups() {
        return helper.getGroups();
    }

    /**
     * Delete Group
     */
    public void deleteGroup(long group_id) {
        helper.deleteGroup(group_id);
    }














}
