package pl.edu.agh.ki.io.alarm.server.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class MessageSerializer {

    private final ObjectMapper serializer;

    public MessageSerializer() {
        this.serializer = new ObjectMapper();
    }

    public String serialize(GcmMessage message) throws JsonProcessingException {
        return serializer.writeValueAsString(message);
    }
}
