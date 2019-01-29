package personal.ex0312.kr.lease.service;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import personal.ex0312.kr.lease.domain.Article;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {
    private final String LEASE_INFO_HTML = "email_content_leases_info.html";
    private final String ROW_HTML = "email_content_row.html";

    private final Gmail gmail;
    private final InternetAddress internetAddress;

    public void sendArticles(String recipientEmailAddress, List<Article> articles) throws IOException, MessagingException {
        StringBuilder rows = new StringBuilder();
        AtomicInteger atomicInteger = new AtomicInteger(0);

        articles.forEach(article -> {
            String price = article.getWarrantPrice();
            if (!StringUtils.isEmpty(article.getMonthlyPrice())) {
                price += "/" + article.getMonthlyPrice();
            }
            String rowHtml = getEmailTemplate(ROW_HTML)
                .replaceAll("##TRADE_TYPE##", article.getTradeType())
                .replaceAll("##KIND##", article.getBuildingType())
                .replaceAll("##EXCLUSIVE_USING_AREA##", String.valueOf(article.getExclusiveUsingArea()))
                .replaceAll("##DIRECTION##", article.getDirection())
                .replaceAll("##FLOOR_INFO##", article.getFloorInfo())
                .replaceAll("##PRICE##", price)
                .replaceAll("##MOBILE_DETAIL_LINK##", article.getMobileDetailLink())
                .replaceAll("##PC_DETAIL_LINK##", article.getPcDetailLink())
                .replaceAll("##COLOR##", atomicInteger.getAndIncrement() % 2 == 0 ? "white" : "#C5FFFF");

            rows.append(rowHtml);
        });

        String leaseInfoTemplate = getEmailTemplate(LEASE_INFO_HTML)
            .replaceAll("##ROWS##", rows.toString());

        Message message = createMessage("매물정보 " + LocalDateTime.now().toString(), leaseInfoTemplate);
        gmail.users().messages().send(recipientEmailAddress, message).execute();

        log.info("Email has sent successfully.");
    }

    private Message createMessage(String subject, String htmlRawStr) throws MessagingException, IOException {
        String encodedEmail = createEncodedRawString(subject, htmlRawStr);
        return new Message().setRaw(encodedEmail);
    }

    private String createEncodedRawString(String subject, String htmlRawStr) throws MessagingException, IOException {
        MimeBodyPart bpHtml = new MimeBodyPart();
        bpHtml.setContent(htmlRawStr, "text/html; charset=utf-8");

        MimeMultipart mimeMultipart = new MimeMultipart("alternative");
        mimeMultipart.addBodyPart(bpHtml);

        MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
        mimeMessage.setFrom(internetAddress);
        mimeMessage.addRecipient(RecipientType.TO, internetAddress);
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(mimeMultipart);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        mimeMessage.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        return Base64.encodeBase64URLSafeString(bytes);
    }

    private String getEmailTemplate(String templateFileName) {
        try {
            return Resources.toString(Resources.getResource(templateFileName), Charsets.UTF_8);
        } catch (IOException e) {
            log.error("An error occurred on loading email template from {}", templateFileName, e);
            throw new RuntimeException();
        }
    }
}
