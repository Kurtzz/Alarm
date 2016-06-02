package pl.edu.agh.ki.io.alarm.server.controllers;

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

@RestController
@RequestMapping(path = "/alarm/message")
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    private final GoogleCloudService gcmService;
    private final UserRepository userRepository;

    @Autowired
    public MessageController(GoogleCloudService messageService, UserRepository userRepository) {
        this.gcmService = messageService;
        this.userRepository = userRepository;
    }

    @RequestMapping(path = "/send/user")
    public HttpStatus sendMessage(@PathVariable String message) {
        try {
            for(User user : userRepository.getAll()) {
                String token = user.getToken();
//                gcmService.sendTo(token, message);
            }
            return HttpStatus.OK;

        } catch (Exception e) {
            LOGGER.warn("Error when sending message", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @RequestMapping(path = "/send/group")
    public HttpStatus sendMessageToGroup() {

    }

}
