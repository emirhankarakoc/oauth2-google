package com.karakoc.sofra.gmail;

import lombok.Data;

@Data
public class SendMailRequest {
    private String accessToken;
    private String subject;
    private String bodyText;
}
