package com.karakoc.sofra.account;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karakoc.sofra.account.requests.LoginRequest;
import com.karakoc.sofra.account.requests.LoginResponse;
import com.karakoc.sofra.account.requests.OAuth2Response;
import com.karakoc.sofra.account.requests.RegisterRequest;
import com.karakoc.sofra.exceptions.general.BadRequestException;
import com.karakoc.sofra.exceptions.general.UnauthorizatedException;
import com.karakoc.sofra.security.TokenManager;
import com.karakoc.sofra.security.UserPrincipal;
import com.karakoc.sofra.user.UserDTO;
import com.karakoc.sofra.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    // Normal giriş
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.attemptLogin(request.getEmail(), request.getPassword());
    }

    // Normal kayıt
    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterRequest request) {
        return authService.attemptRegister(request.getEmail(),request.getName(), request.getPassword());
    }

    // Kullanıcının kendi bilgilerini getirme
    @GetMapping("/getme")
    public UserDTO getMe(@AuthenticationPrincipal UserPrincipal principal) {
        return userService.getUserByEmail(principal.getEmail());
    }


    @PostMapping("/oauth2/{accessToken}")
    public UserDTO registerUserWithOAuth2(@AuthenticationPrincipal UserPrincipal principal,@PathVariable String accessToken,@RequestBody OAuth2Response user){
        System.out.println("User email:"+ principal.getUserId());
        System.out.println("Received access token: " + accessToken);
        System.out.println("Received data: " + user);

        return userService.assignGmailTokenToUser(principal.getUserId(),accessToken,user);

    }


    @PostMapping("/delete/oauth2connection/{userId}")
    public void unlinkGmailAccount(@AuthenticationPrincipal UserPrincipal principal,@PathVariable String userId){
        if (!principal.getUserId().equals(userId)) {
            throw new UnauthorizatedException("User Id's are not matching with logged in and path variable id");
        }
        userService.unlinkGmailAccount(userId);
    }



}
