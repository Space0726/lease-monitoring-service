package personal.ex0312.kr.lease.service;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {
    private final Gmail gmail;
    private final InternetAddress internetAddress;

    public void sendEmail(String subject, String content) throws IOException, MessagingException {
        Message message = createMessage(subject, content);

        gmail.users().messages().send("example0312@gmail.com", message).execute();
        log.info("Email has sent successfully.");
    }

    private Message createMessage(String subject, String content) throws MessagingException, IOException {
        String encodedEmail = createEncodedEmail(subject, content);
        return new Message().setRaw(encodedEmail);
    }

    private String createEncodedEmail(String subject, String content) throws MessagingException, IOException {
        MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
        mimeMessage.setFrom(internetAddress);
        mimeMessage.addRecipient(RecipientType.TO, internetAddress);
        mimeMessage.setSubject(subject);
        mimeMessage.setText(content);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        mimeMessage.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        return Base64.encodeBase64URLSafeString(bytes);
    }
}
