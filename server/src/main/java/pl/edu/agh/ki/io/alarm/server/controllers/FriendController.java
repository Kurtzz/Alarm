package pl.edu.agh.ki.io.alarm.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.agh.ki.io.alarm.domain.User;
import pl.edu.agh.ki.io.alarm.server.registry.UserRepository;

@RequestMapping("/alarm/friend")
public class FriendController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/invite/{senderUid}/{senderToken}/{inviteeUid}")
    public void invite(@PathVariable("senderUid") String senderUid,
                       @PathVariable("senderToken") String senderToken,
                       @PathVariable("inviteeUid") String inviteeUid) {

        User sender = userRepository.get(senderUid);
        if(senderToken.equals(sender.getToken())) {

        }

    }


}
