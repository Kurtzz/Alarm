package pl.edu.agh.io.alarm.sqlite.model;

/**
 * Created by P on 18.05.2016.
 */
public class GroupFriend {
    private int friend_id;
    private int group_id;

    public GroupFriend() {

    }

    public GroupFriend(int friend_id, int group_id) {
        this.friend_id = friend_id;
        this.group_id = group_id;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }
}
