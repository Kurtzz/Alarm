package pl.edu.agh.ki.io.alarm.server;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.ki.io.alarm.server.registry.TokenRegistry;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/alarm")
public class TokenRegistryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenRegistryController.class);

    private final TokenRegistry registry;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Autowired
    public TokenRegistryController(TokenRegistry registry) {
        this.registry = registry;
    }

    @RequestMapping(path = "/tokens/{token}")
    public HttpStatus registerNewToken(@PathVariable String token) throws UnirestException {
        LOGGER.info("Received token: {}", token);
        boolean isNew = registry.add(token);
        if(isNew) {
            LOGGER.info("Token {} has been added to repository", token);
        } else {
            LOGGER.info("Repository already contains token {}", token);
        }
        LOGGER.debug(registry.getAll().toString());

        executor.execute(() -> {
            try {
                Thread.sleep(3000);
                HttpResponse<String> result = Unirest
                        .post("https://gcm-http.googleapis.com/gcm/send")
                        .header("Content-Type", "application/json")
                        .header("Authorization", "key=AIzaSyCTqMXVGZLHRrXW338ZyILk6zT9I1e7S5Q")
                        .body("{\"data\": {\"message\": \"hello, dude!\"}, \"to\": \"" + token + "\"}")
                        .asString();
                LOGGER.info("SENT");
                LOGGER.info("Status: {}, Result: {}\n{}", result.getStatus(), result.getBody(), result.getHeaders());
            } catch (UnirestException e) {
                LOGGER.info("Error", e);
            } catch (InterruptedException e) {
                LOGGER.info("Interrupter", e);
            }
        });

        return HttpStatus.OK;
    }
}
