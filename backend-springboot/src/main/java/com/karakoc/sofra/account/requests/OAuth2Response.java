package com.karakoc.sofra.account.requests;

import lombok.Data;
@Data
public class OAuth2Response {
    private String email;
    private String family_name;
    private String given_name;
    private String id;
    private String name;
    private String picture;
    private Boolean verified_email;
}
