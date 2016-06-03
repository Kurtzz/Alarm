package pl.edu.agh.ki.io.alarm.server.communication;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

public class Invitation {

    private final static AtomicInteger idCounter = new AtomicInteger(10000);

    @Getter
    private final int id;

    @Getter @Setter private String senderUid;
    @Getter @Setter private String inviteeUid;

    private Invitation(int ID) {
        this.id = ID;
    }

    public static Invitation newInvitation() {
        return new Invitation(idCounter.incrementAndGet());
    }
}
