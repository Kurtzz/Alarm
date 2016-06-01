package pl.edu.agh.ki.io.alarm.server.registry;

import pl.edu.agh.ki.io.alarm.domain.User;

public interface UserRepository extends Repository<String, User> {
    boolean containsToken(String token);
}
