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

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/alarm/friend")
@RestController
public class FriendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendController.class);

    private final Map<Integer, Invitation> invitations = new HashMap<>();

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
            int invitationId = storeInvitation(senderUid, inviteeUid);
            GcmMessage message = composeInvitationMessage(inviteeUid, sender, invitationId);
            gcm.send(message);
        }
        return "Invitation sent";
    }


    @RequestMapping(value = "/invitation/accept", method = RequestMethod.POST)
    public void acceptInvitation(@RequestBody Map<String, String> body) throws JsonProcessingException, UnirestException {

        int invitationId = Integer.valueOf(body.get("INVITATION_ID"));
        Invitation invitation = invitations.get(invitationId);
        String senderToken = userRepository.get(invitation.getSenderUid()).getToken();
        String groupId = "FRIEND";
        String inviteeNick = userRepository.get(invitation.getInviteeUid()).getNick();
        GcmMessage message = createInvitationResponseMessage(senderToken, groupId, inviteeNick, InvitationResponse.ACCEPTED, invitation.getInviteeUid());
        gcm.send(message);
        LOGGER.info("User {} accepted invitation from {}", invitation.getInviteeUid(), invitation.getSenderUid());
    }

    @RequestMapping(value = "/invitation/decline", method = RequestMethod.POST)
    public void declineInvitation(@RequestBody Map<String, String> body) throws JsonProcessingException, UnirestException {

        int invitationId = Integer.valueOf(body.get("INVITATION_ID"));
        Invitation invitation = invitations.get(invitationId);
        String senderToken = userRepository.get(invitation.getSenderUid()).getToken();
        String groupId = "FRIEND";
        String inviteeNick = userRepository.get(invitation.getInviteeUid()).getNick();
        GcmMessage message = createInvitationResponseMessage(senderToken, groupId, inviteeNick, InvitationResponse.DECLINED, invitation.getInviteeUid());
        gcm.send(message);
        LOGGER.info("User {} declined invitation from {}", invitation.getInviteeUid(), invitation.getSenderUid());
    }

    private int storeInvitation(String senderUid, String inviteeUid) {
        Invitation invitation = Invitation.newInvitation();
        invitation.setInviteeUid(inviteeUid);
        invitation.setSenderUid(senderUid);
        invitations.put(invitation.getId(), invitation);
        return invitation.getId();
    }

    private GcmMessage createInvitationResponseMessage(String senderToken, String groupId, String inviteeNick, InvitationResponse response, String inviteeUuid) {
        GcmMessage message = new GcmMessage();
        message.setTo(senderToken);
        GcmMessageData data = new GcmMessageData();
        data.setMessageType(MessageType.INVITATION_RESPONSE);
        data.setInvitationResponse(response);
        data.setSenderNick(inviteeNick);
        data.setGroupName(groupId);
        data.setSenderUID(inviteeUuid);
        message.setData(data);
        return message;
    }

    private GcmMessage composeInvitationMessage(String inviteeUid, User sender, int invitationId) {
        String inviteeToken = userRepository.get(inviteeUid).getToken();
        GcmMessage message = new GcmMessage();
        message.setTo(inviteeToken);

        GcmMessageData data = new GcmMessageData();
        data.setInvitationId(invitationId);
        data.setMessageType(MessageType.INVITATION);
        data.setSenderNick(sender.getNick());
        data.setSenderUID(sender.getUID());
        message.setData(data);

        return message;
    }


}
