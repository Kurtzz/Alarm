package pl.edu.agh.io.alarm.sqlite.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by P on 18.05.2016.
 */
public class Group {
    private int id;
    private String groupName;
    private int groupLevel;
    private List<Friend> friends;

    public Group() {
        friends = new ArrayList<>();
    }

    public Group(int id, String groupName, int groupLevel) {
        super();
        this.id = id;
        this.groupName = groupName;
        this.groupLevel = groupLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(int groupLevel) {
        this.groupLevel = groupLevel;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }
}
