package pl.edu.agh.ki.io.alarm.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/alarm/message")
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(path = "/alarm/message/send/{body}")
    public HttpStatus sendToBroadcast(@PathVariable String body) {
        LOGGER.info("Received message: {}, sending to broadcast", body);
        messageService.sendToAll(body);
        return HttpStatus.OK;
    }

}
