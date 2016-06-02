package pl.edu.agh.ki.io.alarm.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.ki.io.alarm.domain.User;
import pl.edu.agh.ki.io.alarm.server.communication.*;
import pl.edu.agh.ki.io.alarm.server.registry.UserRepository;

import java.util.Map;

@RequestMapping("/alarm/friend")
@RestController
public class FriendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoogleCloudService gcm;

    @RequestMapping(value = "/invite", method = RequestMethod.POST)
    public String invite(@RequestBody Map<String, String> body) throws JsonProcessingException, UnirestException {

        String senderUid = body.get(RequestKeys.SENDER_UID);
        String senderToken = body.get(RequestKeys.TOKEN);
        String inviteeUid = body.get(RequestKeys.INVITEE_UID);

        LOGGER.info("Invitation from {} to {}", senderUid, inviteeUid);

        if(!userRepository.getAll().stream()
                .map(User::getUID)
                .filter(inviteeUid::equals)
                .findFirst()
                .isPresent()) {
            LOGGER.warn("Invitee is not int the user repository");
            return "User does not exist";
        }

        User sender = userRepository.get(senderUid);
        if(senderToken.equals(sender.getToken())) {
            // TODO: IS user allowed to invite to the group?
            GcmMessage message = composeInvitationMessage(inviteeUid, sender);
            gcm.send(message);
        }
        return "Invitation sent";
    }

    @RequestMapping(value = "/invitation/accept/")
    public void acceptInvitation(@RequestBody Map<String, String> body) throws JsonProcessingException, UnirestException {

        String senderToken = body.get("SENDER_TOKEN");
        String inviteeUid = body.get("INVITEE_UID");
        String groupId = body.get("GROUP_ID");

        String inviteeNick = userRepository.get(inviteeUid).getNick();

        GcmMessage message = createInvitationResponseMessage(senderToken, groupId, inviteeNick, InvitationResponse.ACCEPTED);

        gcm.send(message);
    }

    @RequestMapping(value = "/invitation/decline/")
    public void declineInvitation(@RequestBody Map<String, String> body) throws JsonProcessingException, UnirestException {

        String senderToken = body.get("SENDER_TOKEN");
        String inviteeUid = body.get("INVITEE_UID");
        String groupId = body.get("GROUP_ID");
        String inviteeNick = userRepository.get(inviteeUid).getNick();
        GcmMessage message = createInvitationResponseMessage(senderToken, groupId, inviteeNick, InvitationResponse.DECLINED);
        gcm.send(message);
    }

    private GcmMessage createInvitationResponseMessage(String senderToken, String groupId, String inviteeNick, InvitationResponse response) {
        GcmMessage message = new GcmMessage();
        message.setTo(senderToken);
        GcmMessageData data = new GcmMessageData();
        data.setMessageType(MessageType.INVITATION_RESPONSE);
        data.setInvitationResponse(response);
        data.setSenderNick(inviteeNick);
        data.setGroupName(groupId);
        message.setData(data);
        return message;
    }

    private GcmMessage composeInvitationMessage(String inviteeUid, User sender) {
        String inviteeToken = userRepository.get(inviteeUid).getToken();
        GcmMessage message = new GcmMessage();
        message.setTo(inviteeToken);

        GcmMessageData data = new GcmMessageData();
        data.setMessageType(MessageType.INVITATION);
        data.setSenderNick(sender.getNick());
        message.setData(data);

        return message;
    }


}
