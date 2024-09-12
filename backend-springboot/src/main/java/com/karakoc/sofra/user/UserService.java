package com.karakoc.sofra.user;


import com.karakoc.sofra.account.requests.OAuth2Response;
import com.karakoc.sofra.oauth2.account.OAuth2Account;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO createUser(String email,String name, String password);
    Optional<UserDTO> findUserByEmail(String email);
    UserDTO getUserByEmail(String email);
    UserDTO getUserById(String id);
    String deleteUser(String email);
    List<UserDTO> getAllUsers();
    UserDTO assignGmailTokenToUser(String userId, String accesstoken, OAuth2Response user);
    void unlinkGmailAccount(String userId);
}
