package pl.edu.agh.ki.io.alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationMain.class);

    public static void main(String ... args){
        LOGGER.info("Initializing application...");
        SpringApplication.run(ApplicationMain.class, args);
    }
}