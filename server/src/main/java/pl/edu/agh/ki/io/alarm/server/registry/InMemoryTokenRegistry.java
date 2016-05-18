package pl.edu.agh.ki.io.alarm.server.registry;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class InMemoryTokenRegistry implements TokenRegistry {

    private final Set<String> tokens;

    public InMemoryTokenRegistry() {
        tokens = new HashSet<>();
    }

    @Override
    public boolean add(String token) {
        return tokens.add(token);
    }

    @Override
    public boolean remove(String token) {
        return tokens.remove(token);
    }

    @Override
    public List<String> getAll() {
        return new ArrayList<>(tokens);
    }
}
