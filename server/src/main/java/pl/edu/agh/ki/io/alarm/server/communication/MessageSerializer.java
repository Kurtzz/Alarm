package pl.edu.agh.ki.io.alarm.server.communication;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class MessageSerializer {

    private final ObjectMapper serializer;

    public MessageSerializer() {
        this.serializer = new ObjectMapper();
        serializer.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public String serialize(GcmMessage message) throws JsonProcessingException {
        return serializer.writeValueAsString(message);
    }
}
