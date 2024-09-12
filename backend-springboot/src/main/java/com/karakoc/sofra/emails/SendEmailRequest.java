package com.karakoc.sofra.emails;

import lombok.Data;

@Data
public class SendEmailRequest {
    private String newsletterId;
    private String subject;
    private String body;
}
