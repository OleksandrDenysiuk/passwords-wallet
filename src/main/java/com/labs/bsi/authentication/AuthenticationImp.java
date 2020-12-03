package com.labs.bsi.authentication;

import com.labs.bsi.model.User;
import com.labs.bsi.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.labs.bsi.cryptoHelper.HMAC.calculateHMAC;
import static com.labs.bsi.cryptoHelper.SHA512.calculateSHA512;

@Service
public class AuthenticationImp implements Authentication{

    private final UserRepo userRepo;

    public AuthenticationImp(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> processLogin(String login, String pass) {
        if(login == null || pass == null || login.isEmpty() || pass.isEmpty()){
            return Optional.empty();
        }
        Optional<User> optionalUser = userRepo.findAll().stream()
                .filter(userItem -> userItem.getLogin().equals(login))
                .findFirst();
        return optionalUser.filter(user -> isPasswordCorrect(pass, user));
    }

    public Optional<User> isLogIn(String login, String pass){
        if(login == null || pass == null || login.isEmpty() || pass.isEmpty()){
            return Optional.empty();
        }

        Optional<User> optionalUser = userRepo.findAll().stream()
                .filter(userItem -> userItem.getLogin().equals(login))
                .findFirst();
        return optionalUser.filter(user -> user.getPassword_hash().equals(pass));
    }

    private static boolean isPasswordCorrect(String passwordIn, User user){
        if(user.getPassword_hash().equals(calculateSHA512("OleksandrDenysiukPepper" + user.getSalt() + passwordIn))){
            return true;
        } else if (user.getPassword_hash().equals(calculateHMAC(passwordIn, "secretKey"))){
            return true;
        } else {
            return false;
        }
    }
}
