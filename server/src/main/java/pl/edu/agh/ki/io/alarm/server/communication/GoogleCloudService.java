package pl.edu.agh.ki.io.alarm.server.communication;

import org.springframework.stereotype.Component;
import pl.edu.agh.ki.io.alarm.server.MessageService;

@Component
public class GoogleCloudService implements MessageService{

    @Override
    public void sendToAll(String message) {

    }
}
