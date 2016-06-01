package pl.edu.agh.ki.io.alarm.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.ki.io.alarm.domain.User;
import pl.edu.agh.ki.io.alarm.server.communication.GcmMessage;
import pl.edu.agh.ki.io.alarm.server.communication.GcmMessageData;
import pl.edu.agh.ki.io.alarm.server.communication.GoogleCloudService;
import pl.edu.agh.ki.io.alarm.server.communication.MessageType;
import pl.edu.agh.ki.io.alarm.server.registry.UserRepository;

@RequestMapping("/alarm/friend")
@RestController
public class FriendController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoogleCloudService gcm;

    @RequestMapping("/invite/{senderUid}/{senderToken}/{inviteeUid}")
    public void invite(@PathVariable("senderUid") String senderUid,
                       @PathVariable("senderToken") String senderToken,
                       @PathVariable("inviteeUid") String inviteeUid) {

        User sender = userRepository.get(senderUid);
        if(senderToken.equals(sender.getToken())) {
            String inviteeToken = userRepository.get(inviteeUid).getToken();
            GcmMessage message = new GcmMessage();
            message.setTo(inviteeToken);
            GcmMessageData data = new GcmMessageData();
            data.setMessageType(MessageType.INVITATION);
            data.set
        }atus

    }


}
