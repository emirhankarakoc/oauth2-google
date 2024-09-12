package com.karakoc.sofra.oauth2.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class OAuth2Account {
    @Id
    private String id;
    private String email;
    private String family_name;
    private String given_name;
    private String gmailId;
    private String name;
    private String picture;
    private Boolean verified_email;
    private String gmailAccessToken;
}
