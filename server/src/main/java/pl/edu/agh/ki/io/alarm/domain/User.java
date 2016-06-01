package pl.edu.agh.ki.io.alarm.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

public class User {

    @Getter private final String UID;

    @Getter @Setter private String token;
    @Getter private final List<User> friends;
    @Getter private final List<String> groupNames;
    @Getter @Setter private String nick;

    public User(String UID) {
        this.UID = UID;
        this.friends = new LinkedList<>();
        this.groupNames = new LinkedList<>();
    }

}
