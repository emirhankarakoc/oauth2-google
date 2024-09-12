package com.karakoc.sofra.customers;

import com.karakoc.sofra.exceptions.general.NotfoundException;
import com.karakoc.sofra.newsletters.Newsletter;
import com.karakoc.sofra.newsletters.NewsletterRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {
    private final NewsletterRepository newsletterRepository;
    private final CustomerRepository customerRepository;

    @PostMapping("/newsletter/{newsletterId}")
    public void subscribeToNewsletter(@PathVariable String newsletterId, @RequestBody CreateCustomerRequest r){
        //niye servis yazayim ki? bence hallolur.
        Newsletter newsletter = newsletterRepository.findById(newsletterId).orElseThrow(()-> new NotfoundException("Newsletter not found."));
        Customer customer = new Customer();

        customer.setId(UUID.randomUUID().toString());
        customer.setName(r.getName());
        customer.setEmail(r.getEmail());
        customer.setNewsletterId(newsletterId);
        customerRepository.save(customer);
        newsletter.getCustomers().add(customer);
        newsletterRepository.save(newsletter);

    }
}
