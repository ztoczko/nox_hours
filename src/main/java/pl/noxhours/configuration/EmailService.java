package pl.noxhours.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.LocaleContextResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

@Log4j2
@Component
@RequiredArgsConstructor
public class EmailService{

    private final JavaMailSender javaMailSender;

    public boolean sendMessage(String email, String subject, String text, String filename) {

        MimeMessage message = javaMailSender.createMimeMessage();
        boolean successFlag = true;
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setText(text, true);
            helper.setFrom("noreply@nox-hours.com");
            helper.setTo(email);
            helper.setSubject(subject);
            if (filename != null) {
                FileSystemResource file = new FileSystemResource((new File(filename)));
                helper.addAttachment(filename, file);
            }
        } catch (MessagingException e) {
            log.error(e.getMessage());
            successFlag = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);
        return successFlag;
    }

}
