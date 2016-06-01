package pl.edu.agh.ki.io.alarm.server.registry;

import org.springframework.stereotype.Component;
import pl.edu.agh.ki.io.alarm.domain.Group;

import java.util.*;

@Component
public class InMemoryGroupRegistry implements GroupRepository {

    private final Map<String, Group> groups;

    public InMemoryGroupRegistry() {
        groups = new HashMap<>();
    }

    @Override
    public Group get(String key) {
        return groups.get(key);
    }

    @Override
    public void add(String uid, Group group) {
        groups.put(uid, group);
    }

    @Override
    public void remove(String uid) {
        groups.remove(uid);
    }

    @Override
    public List<Group> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(groups.values()));
    }

}
