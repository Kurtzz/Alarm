package pl.edu.agh.ki.io.alarm.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.ki.io.alarm.server.registry.TokenRegistry;

@RestController
@RequestMapping("/alarm")
public class TokenRegistryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenRegistryController.class);

    private final TokenRegistry registry;

    @Autowired
    public TokenRegistryController(TokenRegistry registry) {
        this.registry = registry;
    }

    @RequestMapping(path = "/tokens/{token}")
    public HttpStatus registerNewToken(@PathVariable String token) {
        LOGGER.info("Received token: {}", token);
        boolean isNew = registry.add(token);
        if(isNew) {
            LOGGER.info("Token {} has been added to repository", token);
        } else {
            LOGGER.info("Repository already contains token {}", token);
        }
        LOGGER.debug(registry.getAll().toString());

        return HttpStatus.OK;
    }
}
