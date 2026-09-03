package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.PostReaction;
import com.makersacademy.acebook.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PostReactionRepository
        extends CrudRepository<PostReaction, Long> {

    Optional<PostReaction> findByUserAndPost(User user, Post post);

    long countByPostAndReaction(Post post, String reaction);
}