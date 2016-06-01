package pl.edu.agh.ki.io.alarm.server.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.ki.io.alarm.server.communication.GoogleCloudService;
import pl.edu.agh.ki.io.alarm.server.registry.TokenRegistry;
import pl.edu.agh.ki.io.alarm.server.registry.UserRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/alarm")
public class TokenRegistryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenRegistryController.class);

    private final UserRepository userRepository;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final GoogleCloudService gcmService;

    @Autowired
    public TokenRegistryController(UserRepository userRepository, GoogleCloudService gcmService) {
        this.userRepository = userRepository;
        this.gcmService = gcmService;
    }

    // TODO: Should be secure
    @RequestMapping(path = "/tokens/{token}")
    public HttpStatus registerNewToken(@PathVariable String token) throws UnirestException {

        LOGGER.info("Received token: {}", token);
        boolean isNew = userRepository.add(token);
        
        if(isNew) {
            LOGGER.info("Token {} has been added to repository", token);
        } else {
            LOGGER.info("Repository already contains token {}", token);
        }
        LOGGER.debug(userRepository.getAll().toString());
        return HttpStatus.OK;
    }
}
