package pl.noxhours.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Log4j2
@Component
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public boolean sendMessage(String email, String subject, String text, String filename) {

        MimeMessage message = javaMailSender.createMimeMessage();
        boolean successFlag = true;
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setText(text, true);
            helper.setFrom("NOX-Hours <nox.hours@gmail.com>");
            helper.setTo(email);
            helper.setSubject(subject);
            if (filename != null) {
                FileSystemResource file = new FileSystemResource((new File(filename)));
                helper.addAttachment(filename, file);
            }
        } catch (MessagingException e) {
            log.error(e.getMessage());
            successFlag = false;
        }
        javaMailSender.send(message);
        return successFlag;
    }
}
