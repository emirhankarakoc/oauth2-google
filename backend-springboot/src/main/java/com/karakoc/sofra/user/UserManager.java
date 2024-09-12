package com.karakoc.sofra.user;

import com.karakoc.sofra.account.requests.OAuth2Response;
import com.karakoc.sofra.exceptions.general.BadRequestException;
import com.karakoc.sofra.exceptions.general.NotfoundException;
import com.karakoc.sofra.exceptions.strings.ExceptionMessages;
import com.karakoc.sofra.oauth2.account.OAuth2Account;
import com.karakoc.sofra.oauth2.account.OAuth2AccountRepository;
import com.karakoc.sofra.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.karakoc.sofra.user.User.userToDTO;
import static com.karakoc.sofra.user.User.usersToDTOS;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserManager implements UserService{

    private final UserRepository repository;
    private final ExceptionMessages messages;
    private final WebSecurityConfig webSecurityConfig;
    private final OAuth2AccountRepository oAuth2AccountRepository;
    private final UserRepository userRepository;

    @Override
    public UserDTO createUser(String email,String name, String password) {

        if (repository.findUserByEmail(email).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(email);
        user.setUserStatus(UserStatus.GOOGLE_NOT_VERIFICATED);
        OAuth2Account oauthacc = new OAuth2Account();
        oauthacc.setId(UUID.randomUUID().toString());
        oAuth2AccountRepository.save(oauthacc);
        user.setOauth2(oauthacc);
        user.setPassword(webSecurityConfig.passwordEncoder().encode(password));
        user.setRole(Roles.ROLE_USER.toString());
        return User.userToDTO(repository.save(user));
    }
    @Override
    public UserDTO getUserByEmail(String email){
        User user = repository.findUserByEmail(email).orElseThrow(()-> new NotfoundException(messages.getUSER_NOT_FOUND_404()));
        var dto = User.userToDTO(user);
        return dto;
    }

    @Override
    public UserDTO getUserById(String id) {
        User user = repository.findUserByEmail(id).orElseThrow(()-> new NotfoundException(messages.getUSER_NOT_FOUND_404()));
        var dto = User.userToDTO(user);
        return dto;
    }

    @Override
    public String deleteUser(String email){
        User user = repository.findUserByEmail(email).orElseThrow(()-> new NotfoundException(messages.getUSER_NOT_FOUND_404()));
        repository.delete(user);
        return "An user deleted with given email adress:" + user.getEmail();
    }


    public List<UserDTO> getAllUsers(){
        var allusers = repository.findAll();
        return usersToDTOS(allusers);
    }

    @Override
    public Optional<UserDTO> findUserByEmail(String email) {
        User user = repository.findUserByEmail(email).orElseThrow(()-> new NotfoundException(messages.getUSER_NOT_FOUND_404()));
        return Optional.of(userToDTO(user));
    }

    public UserDTO assignGmailTokenToUser(String userId, String accesstoken, OAuth2Response user1) {
        User user = repository.findById(userId).orElseThrow(()-> new NotfoundException(messages.getUSER_NOT_FOUND_404()));
        OAuth2Account gmailAcc = user.getOauth2();
        gmailAcc.setGmailAccessToken(accesstoken);
        gmailAcc.setGmailId(user1.getId());
        gmailAcc.setName(user1.getName());
        gmailAcc.setEmail(user1.getEmail());
        gmailAcc.setPicture(user1.getPicture());
        gmailAcc.setFamily_name(user1.getFamily_name());
        gmailAcc.setGiven_name(user1.getGiven_name());
        gmailAcc.setVerified_email(user1.getVerified_email());
        oAuth2AccountRepository.save(gmailAcc);
        user.setOauth2(gmailAcc);
        user.setUserStatus(UserStatus.GOOGLE_VERIFICATED);
        userRepository.save(user);
        return User.userToDTO(user);
    }

    @Override
    public void unlinkGmailAccount(String userId) {
        // Fetch the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotfoundException("User not found."));

        // Get the associated OAuth2Account
        OAuth2Account oldAccount = user.getOauth2();

        // Create a new placeholder OAuth2Account
        OAuth2Account placeholderAccount = new OAuth2Account();
        placeholderAccount.setId(UUID.randomUUID().toString());

        // Save the placeholder account to ensure it has an ID
        oAuth2AccountRepository.save(placeholderAccount);

        // Assign the placeholder account to the user
        user.setOauth2(placeholderAccount);
        user.setUserStatus(UserStatus.GOOGLE_NOT_VERIFICATED); // Update user status as needed
        // Save the user with the updated OAuth2Account
        userRepository.save(user);

        // Now, safely delete the old account
        oAuth2AccountRepository.delete(oldAccount);
    }


}
