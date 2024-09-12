package com.karakoc.sofra.newsletters;

import java.util.List;

public interface NewsletterService {
    Newsletter createProject(String ownerUserId, String name);

    List<Newsletter> getMyProjects(String ownerUserId);
}
