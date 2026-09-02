package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/profile")
    public String profile(Model model) {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String email = principal.getEmail();

        User user = userRepository
                .findUserByUsername(email)
                .orElseThrow();

        model.addAttribute("user", user);

        return "profile/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam String fullName,
            @RequestParam String email
    ) {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String currentEmail = principal.getEmail();

        User user = userRepository
                .findUserByUsername(currentEmail)
                .orElseThrow();

        user.setFullName(fullName);
        user.setUsername(email);

        userRepository.save(user);

        return "redirect:/profile";
    }
}