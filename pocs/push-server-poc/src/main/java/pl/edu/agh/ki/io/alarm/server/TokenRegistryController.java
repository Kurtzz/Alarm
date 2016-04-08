package pl.edu.agh.ki.io.alarm.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.ki.io.alarm.server.registry.TokenRegistry;

@RestController
@RequestMapping("/alarm")
public class TokenRegistryController {

    private final TokenRegistry registry;

    @Autowired
    public TokenRegistryController(TokenRegistry registry) {
        this.registry = registry;
    }

    public HttpStatus registerNewToken()

}
