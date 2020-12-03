package com.labs.bsi.controller;

import com.labs.bsi.authentication.Authentication;
import com.labs.bsi.model.Password;
import com.labs.bsi.model.User;
import com.labs.bsi.repo.PassRepo;
import com.labs.bsi.repo.UserRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.labs.bsi.cryptoHelper.AESenc.decrypt;
import static com.labs.bsi.cryptoHelper.AESenc.encrypt;
import static com.labs.bsi.cryptoHelper.HMAC.calculateHMAC;
import static com.labs.bsi.cryptoHelper.SHA512.calculateSHA512;

@Controller
public class WalletController {

    private final PassRepo passRepo;
    private final UserRepo userRepo;
    private final Authentication authentication;

    public WalletController(PassRepo passRepo, UserRepo userRepo, Authentication authentication) {
        this.passRepo = passRepo;
        this.userRepo = userRepo;
        this.authentication = authentication;
    }

    @GetMapping("/")
    public String getWallet(@CookieValue(value = "pass_wallet_user_login", defaultValue = "") String login,
                            @CookieValue(value = "pass_wallet_user_pass", defaultValue = "") String password,
                            Model model) {
        Optional<User> optionalUser = authentication.isLogIn(login, password);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("passwords", user.getPasswords());
            return "index";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/")
    public String addNewOne(@RequestParam("web_address") String address,
                            @RequestParam("description") String description,
                            @RequestParam("login") String login,
                            @RequestParam("password") String password,
                            @CookieValue(value = "pass_wallet_user_login", defaultValue = "") String sessionLogin,
                            @CookieValue(value = "pass_wallet_user_pass", defaultValue = "") String sessionPassword) throws Exception {
        User user = userRepo.findByLogin(sessionLogin);
        Password newPass = Password.builder()
                .login(login)
                .password(encrypt(password, sessionPassword))
                .description(description)
                .webAddress(address)
                .user(user).build();
        user.getPasswords().add(newPass);
        passRepo.save(newPass);
        return "redirect:/";
    }

    @PostMapping("/change")
    public String cane(@RequestParam("oldPassword") String oldPassword,
                       @RequestParam("newPassword") String newPassword,
                       @CookieValue(value = "login_wallet", defaultValue = "") String login,
                       @CookieValue(value = "isLogin_wallet", defaultValue = "false") boolean isLogin) throws Exception {
        User user = userRepo.findByLogin(login);
        if (user.isPasswordKeptAsHash()) {
            if (user.getPassword_hash().equals(calculateSHA512("OleksandrDenysiuk" + oldPassword))) {
                user.setPassword_hash(calculateSHA512("OleksandrDenysiuk" + newPassword));
                for (Password password : user.getPasswords()) {
                    //password.setPassword(encrypt(user.getPassword_hash() + decrypt(password.getPassword())));
                }
            }
        } else {
            if (user.getPassword_hash().equals(calculateHMAC(oldPassword, "alex1997"))) {
                user.setPassword_hash(calculateHMAC(oldPassword, "alex1997"));
            }
        }
        userRepo.save(user);
        return "redirect:/";
    }
}
