package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/search")
    public String search(
            @RequestParam(required = false, defaultValue = "") String query,
            Model model
    ) {
        List<User> users = Collections.emptyList();
        List<Post> posts = Collections.emptyList();

        if (!query.isBlank()) {
            users = userRepository
                    .findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(
                            query,
                            query
                    );

            posts = postRepository
                    .findByContentContainingIgnoreCaseOrderByIdDesc(query);
        }

        model.addAttribute("query", query);
        model.addAttribute("users", users);
        model.addAttribute("posts", posts);

        return "search/index";
    }
}