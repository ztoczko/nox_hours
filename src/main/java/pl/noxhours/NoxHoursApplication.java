package pl.noxhours;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.noxhours.user.UserService;


@SpringBootApplication

public class NoxHoursApplication {

    private final Logger log = (Logger) LoggerFactory.getLogger(NoxHoursApplication.class);

    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(NoxHoursApplication.class, args);
    }

}
