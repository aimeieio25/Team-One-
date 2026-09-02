package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void postBelongsToUser() {
        User user = new User();
        user.setUsername("test@user.co.uk");

        userRepository.save(user);

        Post post = new Post("Hello");
        post.setUser(user);

        postRepository.save(post);

        Post savedPost = postRepository.findById(post.getId()).orElseThrow();

        assertEquals(user.getId(), savedPost.getUser().getId());
        assertEquals("test@user.co.uk", savedPost.getUser().getUsername());
    }

    @Test
    public void returnsPostsNewestFirst() {
        Post firstPost = new Post("First");
        Post secondPost = new Post("Second");
        Post thirdPost = new Post("Third");

        postRepository.save(firstPost);
        postRepository.save(secondPost);
        postRepository.save(thirdPost);

        List<Post> posts = postRepository.findAllByOrderByIdDesc();

        assertEquals("Third", posts.get(0).getContent());
        assertEquals("Second", posts.get(1).getContent());
        assertEquals("First", posts.get(2).getContent());
    }
}