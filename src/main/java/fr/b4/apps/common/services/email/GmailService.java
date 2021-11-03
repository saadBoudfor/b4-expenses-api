package fr.b4.apps.common.services.email;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class GmailService {
    private final JavaMailSender emailSender;

    public GmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendMailWithAttachment(String to,
                                       String subject,
                                       String text,
                                       String attachmentName,
                                       String pathToAttachment) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("noreplay@b4expenses.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        FileSystemResource file
                = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment(attachmentName, file);

        emailSender.send(message);
    }
}
