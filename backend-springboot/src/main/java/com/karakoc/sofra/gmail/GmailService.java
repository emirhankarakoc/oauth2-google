package com.karakoc.sofra.gmail;

public interface GmailService {
    void sendEmail(String accessToken, String to, String subject, String bodyText);
}
