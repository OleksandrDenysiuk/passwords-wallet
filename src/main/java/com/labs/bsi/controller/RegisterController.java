package com.labs.bsi.controller;

import com.labs.bsi.model.User;
import com.labs.bsi.repo.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

import static com.labs.bsi.cryptoHelper.HMAC.calculateHMAC;
import static com.labs.bsi.cryptoHelper.SHA512.calculateSHA512;

@Controller
@RequestMapping("/registration")
public class RegisterController {

    private final UserRepo userRepo;

    public RegisterController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public String getPage() {
        return "registration";
    }

    @PostMapping
    public String register(@RequestParam(name = "login") String login,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "typeOfStore") String typeOfStore) {
        if(isExists(login)){
            return "redirect:/registration";
        }else {
            proccesRegister(login, password, typeOfStore);
            return "redirect:/login";
        }
    }


    private boolean isExists(String login){
        Optional <User> optionalUser = userRepo.findAll().stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
        return optionalUser.isPresent();
    }


    private void proccesRegister(String login, String password, String typeOfStore){
        String salt = getRandomAlphabeticSalt();

        if (typeOfStore.equals("SHA512")) {
            User user = User.builder().login(login).password_hash(calculateSHA512("OleksandrDenysiukPepper" + salt + password)).salt(salt).isPasswordKeptAsHash(true).build();
            userRepo.save(user);
        } else {
            User user = User.builder().login(login).password_hash(calculateHMAC(password, "secretKey")).salt(salt).isPasswordKeptAsHash(true).build();
            userRepo.save(user);
        }
    }


    public String getRandomAlphabeticSalt() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 13;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
