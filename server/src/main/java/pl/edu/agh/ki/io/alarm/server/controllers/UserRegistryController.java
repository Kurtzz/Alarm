package pl.edu.agh.ki.io.alarm.server.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.ki.io.alarm.domain.User;
import pl.edu.agh.ki.io.alarm.server.communication.GoogleCloudService;
import pl.edu.agh.ki.io.alarm.server.communication.RequestKeys;
import pl.edu.agh.ki.io.alarm.server.registry.UserRepository;

import java.util.Map;
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
    @RequestMapping(path = "/tokens/add")
    public String registerNewUser(@RequestBody Map<String, String> body) throws UnirestException {

        String token = body.get(RequestKeys.TOKEN);
        String nickname = body.get(RequestKeys.NICKNAME);

        LOGGER.info("Received token: {}", token);
        boolean isNew = !userRepository.containsToken(token);
        User user;

        if(isNew) {
            user = createNewUSer(token, nickname);
            userRepository.add(user.getUID(), user);
            LOGGER.info("User with uid {} and nick {} has been added to repository", user.getUID(), user.getNick());
        } else {
            user = userRepository.getAll().stream().filter(u -> u.getToken().equals(token)).findFirst().orElse(null);
            LOGGER.info("Repository already contains uid {} with token {}", user.getUID(), token);
        }
        LOGGER.debug(userRepository.getAll().toString());
        return user.getUID();
    }

    private User createNewUSer(String token, String nickname) {
        String uid = RandomStringUtils.random(6, true, true); // TODO: Uniqueness
        User user = new User(uid);
        user.setToken(token);
        user.setNick(nickname);
        return user;
    }
}
