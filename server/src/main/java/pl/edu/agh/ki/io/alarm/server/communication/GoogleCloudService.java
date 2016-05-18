package pl.edu.agh.ki.io.alarm.server.communication;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.io.alarm.server.MessageService;
import pl.edu.agh.ki.io.alarm.server.registry.TokenRegistry;

@Component
public class GoogleCloudService implements MessageService{

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCloudService.class);

    private final TokenRegistry tokenRegistry;

    @Autowired
    public GoogleCloudService(TokenRegistry registry) {
        this.tokenRegistry = registry;
    }

    @Override
    public void sendToAll(String message) throws Exception {
        for(String token : tokenRegistry.getAll()) {
            sendTo(token, message);
        }
    }

    @Override
    public void sendTo(String token, String message) throws UnirestException {
        HttpResponse<String> result = Unirest
                .post("https://gcm-http.googleapis.com/gcm/send")
                .header("Content-Type", "application/json")
                .header("Authorization", "key=AIzaSyCTqMXVGZLHRrXW338ZyILk6zT9I1e7S5Q")
                .body("{\"data\": {\"" + message + "\": \"hello, dude!\"}, \"to\": \"" + token + "\"}")
                .asString();
        LOGGER.info("SENT");
        LOGGER.info("Status: {}, Result: {}\n{}", result.getStatus(), result.getBody(), result.getHeaders());
    }
}
