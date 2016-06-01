package pl.edu.agh.ki.io.alarm.server.registry;

import java.util.List;

public interface Repository<K, V> {

    V get(K key);
    void add(K key, V value);
    void remove(K key);
    List<V> getAll();
}
