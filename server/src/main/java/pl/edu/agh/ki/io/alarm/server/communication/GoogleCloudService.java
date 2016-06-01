package pl.edu.agh.ki.io.alarm.server.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.RequestBodyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.io.alarm.server.registry.UserRepository;

@Component
public class GoogleCloudService {

    public static final String GOOGLE_PROJECT_ID = "AIzaSyCTqMXVGZLHRrXW338ZyILk6zT9I1e7S5Q";

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCloudService.class);
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String GCM_SEND_URL = "https://gcm-http.googleapis.com/gcm/send";
    public static final String AUTHORIZATION = "Authorization";

    private final UserRepository userRepository;
    private final MessageSerializer serializer;

    @Autowired
    public GoogleCloudService(MessageSerializer serializer, UserRepository registry) {
        this.userRepository = registry;
        this.serializer = serializer;
    }

    public void send(GcmMessage message) throws UnirestException, JsonProcessingException {

        String messageBody = serializer.serialize(message);
        HttpResponse<String> result = createRequest(messageBody).asString();
        LOGGER.info("Sent message '{}' to {}", message, message.getTo());
    }

    private RequestBodyEntity createRequest(String messageBody) {
        return Unirest
                .post(GCM_SEND_URL)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(AUTHORIZATION, "key=" + GOOGLE_PROJECT_ID)
                .body(messageBody);
    }
}
