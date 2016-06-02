package pl.edu.agh.io.alarm.sqlite.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.List;

import pl.edu.agh.io.alarm.sqlite.helper.DatabaseHelper;
import pl.edu.agh.io.alarm.sqlite.model.Friend;
import pl.edu.agh.io.alarm.sqlite.model.Group;
import pl.edu.agh.io.alarm.sqlite.model.User;

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


    // ------------------------ "friends" table methods ----------------//

    public long createFriend(Friend friend) {
        return helper.createFriend(friend);
    }

    public Friend getFriend(String friend_id) {
        return helper.getFriend(friend_id);
    }

    /**
     * Get all friends
     */
    public List<Friend> getFriends() {
        return helper.getFriends();
    }

    /**
     * Update friend
     */
    public int updateFriend(Friend friend) {
        return helper.updateFriend(friend);
    }

    /**
     * Delete friend
     */
    public void deleteFriend(Friend friend) {
        helper.deleteFriend(friend.getId());

        //Delete all dependencies
        helper.deleteGroupFriend(friend);
    }

    // ------------------------ "group" table methods ----------------//

    /**
     * Create group
     */
    public long createGroup(Group group) {
        long result = helper.createGroup(group);
        createGroupFriend(group.getNameId(), group.getFriends());
        return result;
    }

    /**
     * Update group
     */
    public void updateGroup(Group group) {
        helper.updateGroup(group);

        //Update linking tables
        List<Friend> dbFriends = helper.getAllMembersOfTheGroup(group.getNameId());
        if (dbFriends.equals(group.getFriends())) {
            return;
        }
        for (Friend friend : dbFriends) {
            if (!group.getFriends().contains(friend)) {
                helper.deleteGroupFriend(group, friend);
            }
        }
        for (Friend friend : group.getFriends()) {
            if (!dbFriends.contains(friend)) {
                helper.createGroupFriend(group.getNameId(), friend.getId());
            }
        }
    }

    /**
     * Get single group
     */
    public Group getGroup(String group_id) {
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
    public void deleteGroup(Group group) {
        helper.deleteGroup(group.getNameId());

        //delete dependency
        helper.deleteGroupFriend(group);
    }

    // ------------------------ "group friend" table methods ----------------//

    /**
     * Create multiple group_friend
     */
    public void createGroupFriend(String group_id, List<Friend> friends) {
        for (Friend friend : friends) {
            helper.createGroupFriend(group_id, friend.getId());
        }
    }

    public User getUser() {
        return helper.getUser();
    }
}
