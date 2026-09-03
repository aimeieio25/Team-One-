package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.PostReaction;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.PostReactionRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class PostReactionsController {

    @Autowired
    PostReactionRepository postReactionRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/posts/{postId}/like")
    public String likePost(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        Post post = postRepository.findById(postId)
                .orElseThrow();

        User user = getLoggedInUser(authentication);

        Optional<PostReaction> existingReaction =
                postReactionRepository.findByUserAndPost(user, post);

        if (existingReaction.isPresent()) {

            PostReaction reaction = existingReaction.get();

            if (reaction.getReaction().equals("LIKE")) {

                // They already liked it, so remove the like
                postReactionRepository.delete(reaction);

            } else {

                // They disliked it, so change it to a like
                reaction.setReaction("LIKE");
                postReactionRepository.save(reaction);
            }

        } else {

            // No reaction yet
            PostReaction reaction =
                    new PostReaction(user, post, "LIKE");

            postReactionRepository.save(reaction);
        }

        return "redirect:/posts";
    }

    @PostMapping("/posts/{postId}/dislike")
    public String dislikePost(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        Post post = postRepository.findById(postId)
                .orElseThrow();

        User user = getLoggedInUser(authentication);

        Optional<PostReaction> existingReaction =
                postReactionRepository.findByUserAndPost(user, post);

        if (existingReaction.isPresent()) {

            PostReaction reaction = existingReaction.get();

            if (reaction.getReaction().equals("DISLIKE")) {

                // They already disliked it, so remove the dislike
                postReactionRepository.delete(reaction);

            } else {

                // They liked it, so change it to a dislike
                reaction.setReaction("DISLIKE");
                postReactionRepository.save(reaction);
            }

        } else {

            // No reaction yet
            PostReaction reaction =
                    new PostReaction(user, post, "DISLIKE");

            postReactionRepository.save(reaction);
        }

        return "redirect:/posts";
    }

    private User getLoggedInUser(Authentication authentication) {

        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        String username = oauthUser.getAttribute("email");

        return userRepository
                .findUserByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}