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
import pl.edu.agh.ki.io.alarm.server.MessageService;
import pl.edu.agh.ki.io.alarm.server.registry.TokenRegistry;

@Component
public class GoogleCloudService implements MessageService{

    public static final String GOOGLE_PROJECT_ID = "AIzaSyCTqMXVGZLHRrXW338ZyILk6zT9I1e7S5Q";

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCloudService.class);
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String GCM_SEND_URL = "https://gcm-http.googleapis.com/gcm/send";
    public static final String AUTHORIZATION = "Authorization";

    private final TokenRegistry tokenRegistry;
    private final MessageSerializer serializer;

    @Autowired
    public GoogleCloudService(MessageSerializer serializer, TokenRegistry registry) {
        this.tokenRegistry = registry;
        this.serializer = serializer;
    }

    @Override
    public void sendToAll(String message) throws Exception {
        for(String token : tokenRegistry.getAll()) {
            sendTo(token, message);
        }
    }

    @Override
    public void sendTo(String token, String message) throws UnirestException, JsonProcessingException {

        String messageBody = createMessageBody(token, message);

        HttpResponse<String> result = createRequest(messageBody).asString();
        LOGGER.info("Sent message '{}' to {}", message, token);
    }

    private RequestBodyEntity createRequest(String messageBody) {
        return Unirest
                .post(GCM_SEND_URL)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(AUTHORIZATION, "key=" + GOOGLE_PROJECT_ID)
                .body(messageBody);
    }

    private String createMessageBody(String token, String message) throws JsonProcessingException {
        GcmMessage gcmMessage = new GcmMessage();
        gcmMessage.setTo(token);
        GcmMessageData data = new GcmMessageData();
        data.setMessage(message);
        gcmMessage.setData(data);
        return serializer.serialize(gcmMessage);
    }
}
