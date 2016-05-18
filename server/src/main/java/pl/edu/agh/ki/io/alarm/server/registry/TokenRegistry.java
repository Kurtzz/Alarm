package pl.edu.agh.ki.io.alarm.server.registry;

import java.util.List;

public interface TokenRegistry {

    boolean add(String token);
    boolean remove(String token);
    List<String> getAll();
}
