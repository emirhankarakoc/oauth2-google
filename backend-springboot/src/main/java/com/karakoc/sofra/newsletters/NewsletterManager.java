package com.karakoc.sofra.newsletters;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NewsletterManager implements NewsletterService {
    private final NewsletterRepository newsletterRepository;

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
}
