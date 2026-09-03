package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;

@RestController
public class UsersController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/users/after-login")
    public RedirectView afterLogin() {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String username = (String) principal.getAttributes().get("email");
        String fullName = principal.getFullName();


        User user = userRepository
                .findUserByUsername(username)
                .orElseGet(() -> {
                    User newUser = new User(username, fullName);
                    String handle = username.split("@")[0].toLowerCase();
                    newUser.setHandle(handle);
                    return newUser;
                });


        userRepository.save(user);

        return new RedirectView("/posts");
    }
}