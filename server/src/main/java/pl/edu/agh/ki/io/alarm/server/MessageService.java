package pl.edu.agh.ki.io.alarm.server;

public interface MessageService {

    public void sendToAll(String message) throws Exception;
    public void sendTo(String token, String message) throws Exception;

}
