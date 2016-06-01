package pl.edu.agh.ki.io.alarm.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.ki.io.alarm.server.MessageService;

@RestController
@RequestMapping(path = "/alarm/message")
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    private final MessageService gcmService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.gcmService = messageService;
    }

    @RequestMapping(path = "/send/{message}")
    public HttpStatus sendMessage(@PathVariable String message) {
        try {
            gcmService.sendToAll(message);
            return HttpStatus.OK;
        } catch (Exception e) {
            LOGGER.warn("Error when sending message", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}
