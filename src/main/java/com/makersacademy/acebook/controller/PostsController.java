package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@Controller
public class PostsController {

    @Autowired
    PostRepository repository;

    @GetMapping("/posts")
    public String index(Model model) {
        Iterable<Post> posts = repository.findAllByOrderByIdDesc();
        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());
        return "posts/index";
    }

    @PostMapping("/posts")
    public RedirectView create(@ModelAttribute Post post) {
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
