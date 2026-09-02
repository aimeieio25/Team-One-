package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.PathVariable;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

@Controller
public class PostsController {

    @Autowired
    PostRepository repository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/posts")
    public String index(Model model) {
        Iterable<Post> posts = repository.findAllByOrderByIdDesc();
        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());
        return "posts/index";
    }

    @PostMapping("/posts")
    public RedirectView create(
            @ModelAttribute Post post,
            Authentication authentication
    ) {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String username = oauthUser.getAttribute("email");

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        post.setUser(user);

        repository.save(post);

        return new RedirectView("/posts");
    }
    //Edit and Delete Post functionality
    @GetMapping("/posts/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Post post = repository.findById(id)
                .orElseThrow();

        model.addAttribute("post", post);

        return "posts/edit";
    }
    @PostMapping("/posts/{id}/edit")
    public RedirectView update(
            @PathVariable Long id,
            @ModelAttribute Post post) {

        Post existingPost = repository.findById(id)
                .orElseThrow();

        existingPost.setContent(post.getContent());

        repository.save(existingPost);

        return new RedirectView("/posts");
    }
    @PostMapping("/posts/{id}/delete")
    public RedirectView delete(@PathVariable Long id) {

        repository.deleteById(id);

        return new RedirectView("/posts");
    }
}
