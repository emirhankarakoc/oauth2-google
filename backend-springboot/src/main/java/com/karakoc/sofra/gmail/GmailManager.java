package com.karakoc.sofra.gmail;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

@Service
public class GmailManager implements GmailService{
    private static final String APPLICATION_NAME = "newsletter-service";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // Gmail API'yi başlatmak için OAuth2 access token kullanıyoruz
    private Gmail getGmailService(String accessToken) {
        Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(accessToken);

        return new Gmail.Builder(new NetHttpTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    // Kullanıcı adına e-posta gönderme
    public void sendEmail(String accessToken, String to, String subject, String bodyText) {
        try {
            // Gmail servisini başlat
            Gmail service = getGmailService(accessToken);

            // Kullanıcı email'ini al
            String userEmail = getUserEmailFromToken(service);

            // E-posta mesajını oluştur
            MimeMessage email = createEmail(to, userEmail, subject, bodyText);

            // E-postayı gönder
            sendMessage(service, "me", email);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    // E-posta oluşturma fonksiyonu
    private MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);

        return email;
    }

    // E-postayı Gmail API ile gönder
    private void sendMessage(Gmail service, String userId, MimeMessage email) throws MessagingException, IOException {
        Message message = createMessageWithEmail(email);
        service.users().messages().send(userId, message).execute();
    }

    // E-posta mesajını Gmail API'ye uygun formata dönüştür
    private Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

        Message message = new Message();
        message.setRaw(encodedEmail);

        return message;
    }

    // Kullanıcının token ile gelen e-posta adresini al
    private String getUserEmailFromToken(Gmail service) throws IOException {
        return service.users().getProfile("me").execute().getEmailAddress();
    }
}
