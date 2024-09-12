package com.karakoc.sofra.user;

import com.karakoc.sofra.oauth2.account.OAuth2Account;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String id;
    private String email;
    private String role;
    private String extraInfo;
    private String name;
    private OAuth2Account oauth2;
    private UserStatus userStatus;

}
