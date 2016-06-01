package pl.edu.agh.ki.io.alarm.domain;

import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Group {

    private final Set<User> members;
    @Getter private final String name;

    public Group(String name) {
        this.name = name;
        this.members = new HashSet<>();
    }

    public Collection<User> getAll() {
        return Collections.unmodifiableSet(members);
    }

    public boolean belongs(User user) {
        return members.contains(user);
    }

    public boolean add(User user) {
        return members.add(user);
    }

    public boolean remove(User user) {
        return members.remove(user);
    }
}
