package com.labs.bsi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

import static com.labs.bsi.cryptoHelper.AESenc.decrypt;
import static com.labs.bsi.cryptoHelper.AESenc.encrypt;

@RestController
public class PasswordController {
    public static final String BASE_URL = "/api";

    @GetMapping("/api/decrypt")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getDecryptedPass(@RequestParam("pass") String password,
                                                @CookieValue(value = "pass_wallet_user_login", defaultValue = "") String sessionLogin,
                                                @CookieValue(value = "pass_wallet_user_pass", defaultValue = "") String sessionPassword) throws Exception {
        return Collections.singletonMap("decryptedPassword", decrypt(password, sessionPassword));
    }

    @GetMapping("/api/encrypt/{pass}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getEncryptedPass(@PathVariable("pass") String password,
                                                @CookieValue(value = "pass_wallet_user_login", defaultValue = "") String sessionLogin,
                                                @CookieValue(value = "pass_wallet_user_pass", defaultValue = "") String sessionPassword) throws Exception {
        return Collections.singletonMap("decryptedPassword", encrypt(password, sessionPassword));
    }

    private class SimpleResponse{
        String decryptedPassword;
    }
}
