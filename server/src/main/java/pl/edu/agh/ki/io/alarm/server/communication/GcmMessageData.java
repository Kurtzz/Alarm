package pl.edu.agh.ki.io.alarm.server.communication;

import lombok.Getter;
import lombok.Setter;

public class GcmMessageData {

    @Getter @Setter private MessageType messageType;
    @Getter @Setter private String message;
    @Getter @Setter private String senderNick;
    @Getter @Setter private String senderUID;
    @Getter @Setter private String groupName;
    @Getter @Setter private InvitationResponse invitationResponse;
}
