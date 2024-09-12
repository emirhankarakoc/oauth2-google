import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.karakoc.sofra.customers.Customer;
import com.karakoc.sofra.customers.CustomerRepository;
import com.karakoc.sofra.emails.SendEmailRequest;
import com.karakoc.sofra.exceptions.general.NotfoundException;
import com.karakoc.sofra.exceptions.general.UnauthorizatedException;
import com.karakoc.sofra.newsletters.Newsletter;
import com.karakoc.sofra.newsletters.NewsletterRepository;
import com.karakoc.sofra.security.UserPrincipal;
import com.karakoc.sofra.user.User;
import com.karakoc.sofra.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

@RequestMapping("/emails")
@RestController
@AllArgsConstructor
public class EmailController {
    private final UserRepository userRepository;
    private final NewsletterRepository newsletterRepository;
    private final CustomerRepository customerRepository;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "Your Application Name";

    @PostMapping("/send/{newsletterId}")
    public String sendEmail(@AuthenticationPrincipal UserPrincipal principal, @PathVariable String newsletterId, @RequestBody SendEmailRequest r) {
        User user = userRepository.findById(principal.getUserId()).orElseThrow(() -> new NotfoundException("User not found."));
        Newsletter newsletter = newsletterRepository.findById(newsletterId).orElseThrow(() -> new NotfoundException("Newsletter not found."));

        if (!newsletter.getOwnerUserId().equals(user.getId())) {
            throw new UnauthorizatedException("You don't have permission for sending data to this newsletter.");
        }

        List<Customer> customers = newsletter.getCustomers();
        for (Customer customer : customers) {
            try {
                // Gmail servisini oluştur
                Gmail service = getGmailService(user.getOauth2().getGmailAccessToken());

                // Düz metin içeren e-posta oluştur
                MimeMessage email = createEmail(customer.getEmail(), "me", r.getSubject(), r.getBody());

                // E-posta gönder
                sendMessage(service, "me", email);
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed to send email: " + e.getMessage();
            }
        }
        return "Emails sent successfully!";
    }

    private Gmail getGmailService(String accessToken) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(accessToken, null))
                .createScoped(List.of(GmailScopes.GMAIL_SEND));

        return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText); // Düz metin içerik ekleme
        return email;
    }

    private void sendMessage(Gmail service, String userId, MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);
        Message message = new Message();
        message.setRaw(encodedEmail);

        service.users().messages().send(userId, message).execute();
    }
}
