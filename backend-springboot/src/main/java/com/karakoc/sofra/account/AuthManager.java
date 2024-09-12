package com.karakoc.sofra.account;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karakoc.sofra.account.requests.LoginResponse;
import com.karakoc.sofra.account.requests.OAuth2Response;
import com.karakoc.sofra.exceptions.general.BadRequestException;
import com.karakoc.sofra.security.WebSecurityConfig;
import com.karakoc.sofra.user.User;
import com.karakoc.sofra.user.UserRepository;
import com.karakoc.sofra.user.UserService;
import com.karakoc.sofra.exceptions.general.ForbiddenException;
import com.karakoc.sofra.user.UserDTO;
import com.karakoc.sofra.security.TokenManager;
import com.karakoc.sofra.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.karakoc.sofra.user.User.userToDTO;


@Service

@RequiredArgsConstructor
public class AuthManager implements AuthService{
    private final TokenManager tokenManager;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final WebSecurityConfig webSecurityConfig;

    public LoginResponse attemptLogin(String email, String password) {
       try{
           var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
           SecurityContextHolder.getContext().setAuthentication(authentication);

           var principal = (UserPrincipal) authentication.getPrincipal();
           var roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

           var token = tokenManager.issue(principal.getUserId(), principal.getEmail(), roles);
           return LoginResponse.builder()
                   .accessToken(token)
                   .build();
       }
       catch (Exception e){
           throw new ForbiddenException("Wrong email or password");
           //i found the error, in 24. row but i cant fixed. so i found this solution. thanks.
       }
    }


    public UserDTO attemptRegister(String email,String name, String password){
        return userService.createUser(email,name,password);
    }




}
