package pl.edu.agh.ki.io.alarm.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.ki.io.alarm.domain.User;
import pl.edu.agh.ki.io.alarm.server.communication.GcmMessage;
import pl.edu.agh.ki.io.alarm.server.communication.GcmMessageData;
import pl.edu.agh.ki.io.alarm.server.communication.GoogleCloudService;
import pl.edu.agh.ki.io.alarm.server.communication.MessageType;
import pl.edu.agh.ki.io.alarm.server.registry.UserRepository;

import java.util.Map;

@RequestMapping("/alarm/friend")
@RestController
public class FriendController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoogleCloudService gcm;

    @RequestMapping(value = "/invite", method = RequestMethod.POST)
    public void invite(@RequestBody Map<String, String> body) throws JsonProcessingException, UnirestException {

        String senderUid = body.get("SENDER_UID");
        String senderToken = body.get("SENDER_TOKEN");
        String inviteeUid = body.get("INVITEE_UID");
        String groupId = body.get("GROUP_ID");

        User sender = userRepository.get(senderUid);
        if(senderToken.equals(sender.getToken())) {
            // TODO: IS user allowed to invite to the group?
            GcmMessage message = composeInvitationMessage(inviteeUid, groupId, sender);
            gcm.send(message);
        }

    }

    private GcmMessage composeInvitationMessage(String inviteeUid, String groupId, User sender) {
        String inviteeToken = userRepository.get(inviteeUid).getToken();
        GcmMessage message = new GcmMessage();
        message.setTo(inviteeToken);

        GcmMessageData data = new GcmMessageData();
        data.setMessageType(MessageType.INVITATION);
        data.setSenderNick(sender.getNick());
        data.setGroupName(groupId);
        message.setData(data);

        return message;
    }


}
