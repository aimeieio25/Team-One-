package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Comment;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.CommentRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.servlet.view.RedirectView;

@Controller
public class CommentsController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/posts/{postId}/comments")
    public String createComment(@PathVariable Long postId, @ModelAttribute Comment comment, Authentication authentication) {

        Post post = postRepository.findById(postId)
                .orElseThrow();

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String username = oauthUser.getAttribute("email");

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        comment.setPost(post);
        comment.setUser(user);

        commentRepository.save(comment);

        return "redirect:/posts";
    }

    @PostMapping("/comments/{commentId}/delete")
    public RedirectView delete(@PathVariable Long commentId) {
        commentRepository.deleteById(commentId);

        return new RedirectView("/posts");
    }

    @PostMapping("/comments/{commentId}/edit")
    public RedirectView update(
            @PathVariable Long commentId,
            @ModelAttribute Comment comment) {

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow();

        existingComment.setContent(comment.getContent());

        commentRepository.save(existingComment);

        return new RedirectView("/posts");
    }
}
