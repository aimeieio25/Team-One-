package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Comment;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.CommentRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String createComment(@PathVariable Long postId, @RequestParam Long userId, Comment comment) {
        Post post = postRepository.findById(postId).orElseThrow();
        comment.setPost(post);

        User user = userRepository.findById(userId).orElseThrow();
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
