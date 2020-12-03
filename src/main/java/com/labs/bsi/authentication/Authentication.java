package com.labs.bsi.authentication;

import com.labs.bsi.model.User;

import java.util.Optional;

public interface Authentication {
    Optional<User> processLogin(String login, String pass);

    Optional<User> isLogIn(String login, String password);
}
