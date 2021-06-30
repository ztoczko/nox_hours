package pl.noxhours;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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



//    @Override
//    public void run(String... args) throws Exception {
//
//
//        User user = new User(null, "aaa", "bbb", "zzads@wp.pl", "aaaaaaaa", false, null, (byte) 1, "BS", false, false, LocalDateTime.of(2022, 12, 1, 12, 0, 0));
//        userService.create(user);
//        System.out.println("Created: " + user);
//        System.out.println("-----");
//        System.out.println("Loaded by id:" + userService.read(user.getId()));
//        System.out.println("-----");
//        System.out.println("Loaded by email:" + userService.read("zzads@wp.pl"));
//        System.out.println("-----");
//        System.out.println("Loaded by invalid id:" + userService.read(0L));
//        System.out.println("Loaded by invalid email:" + userService.read("aaaa@wp.pl"));
//        log.debug("debug");
//        log.warn("warn");
//        log.error("error");
//    }
}
