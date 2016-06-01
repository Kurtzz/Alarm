package pl.edu.agh.ki.io.alarm.server.registry;

import org.springframework.stereotype.Component;
import pl.edu.agh.ki.io.alarm.domain.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserRegistry implements UserRepository {

    private final Map<String, User> users;

    public InMemoryUserRegistry() {
        users = new HashMap<>();
    }

    @Override
    public User get(String key) {
        return users.get(key);
    }

    @Override
    public void add(String uid, User user) {
        users.put(uid, user);
    }

    @Override
    public void remove(String uid) {
        users.remove(uid);
    }

    @Override
    public List<User> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(users.values()));
    }

    @Override
    public boolean containsToken(String token) {
        return users.values().stream()
                .map(User::getToken)
                .collect(Collectors.toList())
                .contains(token);
    }
}
