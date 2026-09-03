package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.FriendRequest;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.FriendRequestRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    UserRepository userRepository;

    @ModelAttribute("friendRequests")
    public List<FriendRequest> friendRequests() {

        if (SecurityContextHolder.getContext().getAuthentication() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return List.of();
        }

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof DefaultOidcUser)) {
            return List.of();
        }

        DefaultOidcUser oidcUser = (DefaultOidcUser) principal;

        String username = (String) oidcUser.getAttributes().get("email");

        User currentUser = userRepository
                .findUserByUsername(username)
                .orElse(null);

        if (currentUser == null) {
            return List.of();
        }

        return friendRequestRepository
                .findByReceiverAndStatus(currentUser, "pending");
    }
}
