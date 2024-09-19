package com.karakoc.sofra.newsletters;

import com.karakoc.sofra.gmail.SendMailRequest;
import com.karakoc.sofra.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface NewsletterService {
    Newsletter createProject(String ownerUserId, String name);

    List<Newsletter> getMyProjects(String ownerUserId);
    ResponseEntity sendMessageToSubscribers(String userId,String newsletterId, SendMailRequest r);
}
