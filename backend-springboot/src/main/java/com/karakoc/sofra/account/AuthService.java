package com.karakoc.sofra.account;

import com.fasterxml.jackson.databind.JsonNode;
import com.karakoc.sofra.account.requests.LoginResponse;
import com.karakoc.sofra.account.requests.OAuth2Response;
import com.karakoc.sofra.user.UserDTO;
import org.springframework.stereotype.Service;

public interface AuthService {
    LoginResponse attemptLogin(String email, String password);
    UserDTO attemptRegister(String email,String name, String password);
}
