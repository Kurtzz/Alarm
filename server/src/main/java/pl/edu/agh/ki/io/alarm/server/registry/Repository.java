package pl.edu.agh.ki.io.alarm.server.registry;

import java.util.List;

public interface Repository<K, V> {

    boolean add(K key, V value);
    boolean remove(K key);
    List<V> getAll();
}
