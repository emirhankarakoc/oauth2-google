package com.karakoc.sofra.newsletters;

import com.karakoc.sofra.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/newsletters")
@AllArgsConstructor
public class NewsletterController {
private final NewsletterService newsletterService;

    @PostMapping
    public Newsletter postProject(@AuthenticationPrincipal UserPrincipal principal, @RequestBody String name) {
        return newsletterService.createProject(principal.getUserId(),name);
    }

    @GetMapping("/my")
    public List<Newsletter> getMyProjects(@AuthenticationPrincipal UserPrincipal principal) {
        return newsletterService.getMyProjects(principal.getUserId());
    }

}
