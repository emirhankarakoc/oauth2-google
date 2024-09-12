package com.karakoc.sofra.exceptions.strings;

import lombok.Data;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ExceptionMessages {
    private final String EMAIL_ADRESS_EXISTING_400 = "This e-mail adress is already used.";
    private final String USER_NOT_FOUND_404 = "User not found.";
    private final String FOOD_NOT_FOUND_404 = "Food Ad not found.";
    private final String LOG_IN_FIRST= "You must logged in for send this request.";
    private final String LOW_BALANCE="You don't have money for buy,you should add balance to your account.";
}
