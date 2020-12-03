package com.labs.bsi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.labs.bsi.authentication.Authentication;

import com.labs.bsi.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@Controller
@RequestMapping("/login")
public class LoginController {

    private final Authentication authentication;

    public LoginController(Authentication authentication) {
        this.authentication = authentication;
    }

    @GetMapping
    public String getPage() {
        return "login";
    }

    @PostMapping
    public String getLoginData(@RequestParam(name = "login") String login,
                               @RequestParam(name = "password") String password,
                               HttpServletResponse response) {
        Optional<User> optionalUser = authentication.processLogin(login, password);
        if (optionalUser.isPresent()) {
            response.addCookie(new Cookie("pass_wallet_user_login", optionalUser.get().getLogin()));
            response.addCookie(new Cookie("pass_wallet_user_pass", optionalUser.get().getPassword_hash()));
            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }


}
