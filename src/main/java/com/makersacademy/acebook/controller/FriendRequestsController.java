package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.FriendRequest;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.FriendRequestRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class FriendRequestsController {

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    UserRepository userRepository;

    private User getCurrentUser() {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String username = (String) principal.getAttributes().get("email");

        return userRepository
                .findUserByUsername(username)
                .orElseThrow();
    }

    @GetMapping("/friends")
    public String friends(
            @RequestParam(required = false, defaultValue = "") String query,
            Model model
    ) {

        User currentUser = getCurrentUser();

        List<User> searchResults = Collections.emptyList();

        if (!query.isBlank()) {
            searchResults =
                    userRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrHandleContainingIgnoreCase(
                            query,
                            query,
                            query
                    );
            searchResults.removeIf(user ->
                    user.getId().equals(currentUser.getId())
            );

            searchResults.removeIf(user ->
                    friendRequestRepository.existsBySenderAndReceiverOrSenderAndReceiver(
                            currentUser,
                            user,
                            user,
                            currentUser
                    )
            );
        }

        List<FriendRequest> requests =
                friendRequestRepository.findByReceiverAndStatus(currentUser, "pending");

        List<FriendRequest> acceptedRequests =
                friendRequestRepository.findBySenderAndStatusOrReceiverAndStatus(
                        currentUser,
                        "accepted",
                        currentUser,
                        "accepted"
                );

        List<User> friends = new ArrayList<>();

        for (FriendRequest request : acceptedRequests) {
            if (request.getSender().getId().equals(currentUser.getId())) {
                friends.add(request.getReceiver());
            } else {
                friends.add(request.getSender());
            }
        }

        model.addAttribute("requests", requests);
        model.addAttribute("friends", friends);
        model.addAttribute("acceptedRequests", acceptedRequests);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("query", query);
        model.addAttribute("searchResults", searchResults);
        return "posts/friends";
    }

    @PostMapping("/friend-requests")
    public RedirectView create(@RequestParam String receiverEmail) {

        User sender = getCurrentUser();

        User receiver = userRepository
                .findUserByUsername(receiverEmail)
                .orElseThrow();

        boolean requestExists =
                friendRequestRepository.existsBySenderAndReceiverOrSenderAndReceiver(
                        sender,
                        receiver,
                        receiver,
                        sender
                );

        if (requestExists) {
            return new RedirectView("/friends");
        }

        FriendRequest request = new FriendRequest(sender, receiver);

        friendRequestRepository.save(request);

        return new RedirectView("/friends?sent=true");
    }

    @PostMapping("/friend-requests/{id}/accept")
    public RedirectView accept(@PathVariable Long id) {

        FriendRequest request = friendRequestRepository
                .findById(id)
                .orElseThrow();

        request.setStatus("accepted");

        friendRequestRepository.save(request);

        return new RedirectView("/friends");
    }

    @PostMapping("/friend-requests/{id}/deny")
    public RedirectView deny(@PathVariable Long id) {

        friendRequestRepository.deleteById(id);

        return new RedirectView("/friends");
    }

    @PostMapping("/friends/{id}/delete")
    public RedirectView deleteFriend(@PathVariable Long id) {

        friendRequestRepository.deleteById(id);

        return new RedirectView("/friends");
    }

    @PostMapping("/friend-requests/{id}/deny")
    public RedirectView deny(@PathVariable Long id) {

        FriendRequest request = friendRequestRepository
                .findById(id)
                .orElseThrow();

        request.setStatus("denied");

        friendRequestRepository.save(request);

        return new RedirectView("/");
    }


}
