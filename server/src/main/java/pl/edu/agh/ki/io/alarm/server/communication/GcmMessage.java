package pl.edu.agh.ki.io.alarm.server.communication;

import lombok.Getter;
import lombok.Setter;

public class GcmMessage {

    @Getter @Setter private GcmMessageData data;
    @Getter @Setter private String to;
}
