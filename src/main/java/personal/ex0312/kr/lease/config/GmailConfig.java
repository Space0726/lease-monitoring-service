package personal.ex0312.kr.lease.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GmailConfig {
    @Bean
    public Gmail createGmail() throws GeneralSecurityException, IOException {
        final NetHttpTransport netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Gmail.Builder(netHttpTransport, JacksonFactory.getDefaultInstance(), getCredentials(netHttpTransport))
            .setApplicationName("lease-monitoring-service")
            .build();
    }

    @Bean
    public InternetAddress createInternetAddress() throws AddressException {
        return new InternetAddress("example0312@gmail.com");
    }

    private Credential getCredentials(final NetHttpTransport netHttpTransport) throws IOException {
        InputStream in = GmailConfig.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            netHttpTransport, JacksonFactory.getDefaultInstance(), clientSecrets, Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM))
            .setAccessType("offline")
            .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("example0312@gmail.com");
    }
}
