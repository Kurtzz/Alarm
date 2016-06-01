package pl.edu.agh.ki.io.alarm.server.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.ki.io.alarm.domain.User;
import pl.edu.agh.ki.io.alarm.server.communication.GoogleCloudService;
import pl.edu.agh.ki.io.alarm.server.registry.UserRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/alarm")
public class UserRegistryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistryController.class);

    private final UserRepository userRepository;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final GoogleCloudService gcmService;

    @Autowired
    public UserRegistryController(UserRepository userRepository, GoogleCloudService gcmService) {
        this.userRepository = userRepository;
        this.gcmService = gcmService;
    }

    // TODO: Should be secure
    @RequestMapping(path = "/tokens/{token}/{nickname}")
    public HttpStatus registerNewUser(@PathVariable String token, @PathVariable String nickname) throws UnirestException {

        LOGGER.info("Received token: {}", token);
        boolean isNew = userRepository.containsToken(token);

        if(isNew) {
            User user = createNewUSer(token, nickname);
            userRepository.add(user.getUID(), user);
            LOGGER.info("User with uid {} has been added to repository", user.getUID());
        } else {
            LOGGER.info("Repository already contains token {}", token);
        }
        LOGGER.debug(userRepository.getAll().toString());
        return HttpStatus.OK;
    }

    private User createNewUSer(String token, String nickname) {
        String uid = RandomStringUtils.random(6, true, true); // TODO: Uniqueness
        User user = new User(uid);
        user.setToken(token);
        user.setNick(nickname);
        return user;
    }
}
