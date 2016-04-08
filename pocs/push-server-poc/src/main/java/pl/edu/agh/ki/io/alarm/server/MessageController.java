package pl.edu.agh.ki.io.alarm.server;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/alarm/message")
public class MessageController {

    @RequestMapping()
    public HttpStatus sendToBroadcast() {

    }

}
