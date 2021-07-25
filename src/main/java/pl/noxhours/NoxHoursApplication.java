package pl.noxhours;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.user.User;
import pl.noxhours.user.UserRepository;
import pl.noxhours.user.UserService;

import java.time.LocalDateTime;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
//public class NoxHoursApplication implements CommandLineRunner {
public class NoxHoursApplication {

    private final Logger log = (Logger) LoggerFactory.getLogger(NoxHoursApplication.class);

    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(NoxHoursApplication.class, args);
    }

//    public MessageSource messageSource() {
//        ResourceBundleMessageSource messageSource= new ResourceBundleMessageSource();
//        messageSource.setBasename("");
//    }

//    TODO - searchable inputs dla każdego widoku, docker

}
