package pl.edu.agh.ki.io.alarm.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.ki.io.alarm.domain.Group;
import pl.edu.agh.ki.io.alarm.domain.User;
import pl.edu.agh.ki.io.alarm.server.communication.*;
import pl.edu.agh.ki.io.alarm.server.registry.GroupRepository;
import pl.edu.agh.ki.io.alarm.server.registry.UserRepository;

import java.util.Map;

@RestController
@RequestMapping(path = "/alarm/message")
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    private final GoogleCloudService gcmService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public MessageController(GoogleCloudService messageService, UserRepository userRepository, GroupRepository groupRepository) {
        this.gcmService = messageService;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @RequestMapping(path = "/send/user")
    public HttpStatus sendMessage(@RequestBody Map<String, String> requestBody) {
        try {

            GcmMessage gcmMessage = composeMessage(requestBody);
            gcmMessage.setTo(requestBody.get(RequestKeys.MESSAGE_RECEIVER));
            gcmService.send(gcmMessage);
            LOGGER.info("Sent message to {}", gcmMessage.getTo());
            return HttpStatus.OK;
        } catch (Exception e) {
            LOGGER.warn("Error when sending message", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @RequestMapping(path = "/send/group")
    public HttpStatus sendMessageToGroup(@RequestBody Map<String, String> requestBody) throws JsonProcessingException, UnirestException {
        GcmMessage gcmMessage = composeMessage(requestBody);
        String groupName = requestBody.get(RequestKeys.MESSAGE_RECEIVER);
        Group group = groupRepository.get(groupName);
        gcmMessage.getData().setGroupName(requestBody.get(RequestKeys.GROUP_NAME));

        String senderUid = requestBody.get(RequestKeys.SENDER_UID);
        if(group.getOwnerUid().equals(senderUid)) {
            sendToGroup(gcmMessage, group, senderUid);
        }
        return HttpStatus.OK;
    }

    private GcmMessage composeMessage(Map<String, String> requestBody) {
        String message = requestBody.get(RequestKeys.MESSAGE);
        String senderNick = requestBody.get(RequestKeys.NICKNAME);
        String senderUid = requestBody.get(RequestKeys.SENDER_UID);

        GcmMessage gcmMessage = new GcmMessage();
        GcmMessageData data = new GcmMessageData();
        data.setMessage(message);
        data.setMessageType(MessageType.MESSAGE);
        data.setSenderNick(senderNick);
        data.setSenderUID(senderUid);
        gcmMessage.setData(data);
        return gcmMessage;
    }

    private void sendToGroup(GcmMessage gcmMessage, Group group, String senderUid) throws UnirestException, JsonProcessingException {
        for(User user : group.getAll()) {
            if(!user.getUID().equals(senderUid)) {
                gcmMessage.setTo(user.getToken());
                gcmService.send(gcmMessage);
            }
        }
    }

}
