package com.karakoc.sofra.newsletters;

import com.karakoc.sofra.customers.Customer;
import com.karakoc.sofra.exceptions.general.BadRequestException;
import com.karakoc.sofra.exceptions.general.ForbiddenException;
import com.karakoc.sofra.exceptions.general.NotfoundException;
import com.karakoc.sofra.exceptions.general.UnauthorizatedException;
import com.karakoc.sofra.gmail.GmailManager;
import com.karakoc.sofra.gmail.GmailService;
import com.karakoc.sofra.gmail.SendMailRequest;
import com.karakoc.sofra.user.User;
import com.karakoc.sofra.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NewsletterManager implements NewsletterService {
    private final NewsletterRepository newsletterRepository;
    private final UserRepository userRepository;
    private final GmailService gmailService;

    @Override
    public Newsletter createProject(String ownerUserId, String name) {
        Newsletter newsletter = new Newsletter();
        newsletter.setId(UUID.randomUUID().toString());
        newsletter.setName(name);
        newsletter.setCustomers(new ArrayList<>());
        newsletter.setOwnerUserId(ownerUserId);
        newsletterRepository.save(newsletter);
        return newsletter;
    }

    @Override
    public List<Newsletter> getMyProjects(String ownerUserId) {
        return newsletterRepository.findAllByOwnerUserId(ownerUserId);
    }

    @Override
    public ResponseEntity sendMessageToSubscribers(String userId, String newsletterId, SendMailRequest r) {
        Newsletter newsletter = newsletterRepository.findById(newsletterId).orElseThrow(()->new NotfoundException("Newsletter not found."));
        User user = userRepository.findById(userId).orElseThrow(()-> new NotfoundException("User not found."));
        if (user.getOauth2().getGmailAccessToken().isEmpty()){
            throw  new BadRequestException("You must connect your gmail account.");
        }

        int sentMailCounter = 0;

        if (newsletter.getOwnerUserId().equals(user.getId())) {

            String accessToken = user.getOauth2().getGmailAccessToken();
            List<Customer> subscribers = newsletter.getCustomers();
            for (Customer customer : subscribers) {
                try{
                    gmailService.sendEmail(accessToken,customer.getEmail(),r.getSubject(),r.getBodyText());
                    sentMailCounter++;

                }
                catch (Exception e){
                    throw new BadRequestException("Mail sending failed. Error: "+e.getMessage());
                }
            }


        }
        else{
            throw new ForbiddenException("Forbidden.");
        }

        return ResponseEntity.ok(sentMailCounter + " mails sent successfully!");
    }
}
