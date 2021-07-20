package pl.noxhours.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleContextResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Log4j2
@Component
@RequiredArgsConstructor
public class EmailService{

    private final JavaMailSender javaMailSender;

    public boolean sendMessage(String email, String subject, String text) {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        boolean successFlag = true;
        try {
            helper.setText(text, true);
            helper.setFrom("noreply@nox-hours.com");
            helper.setTo(email);
            helper.setSubject(subject);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            successFlag = false;
        }
        javaMailSender.send(message);
        return successFlag;
    }

}
