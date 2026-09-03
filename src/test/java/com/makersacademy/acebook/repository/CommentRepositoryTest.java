package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Comment;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void commentGetsCreated() {

        User user = new User();
        user.setUsername("fred@gmail.com");
        user.setFullName("Fred Tester");
        user.setEnabled(true);

        userRepository.save(user);

        Post post = new Post();
        post.setContent("Test post");
        post.setUser(user);

        postRepository.save(post);

        Comment comment = new Comment();
        comment.setContent("Test comment.");
        comment.setUser(user);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        Comment foundComment = commentRepository
                .findById(savedComment.getId())
                .orElseThrow();

        assertEquals("Test comment.", foundComment.getContent());
    }

    @Test
    public void commentBelongsToUserAndPost() {
        User user = new User();
        user.setUsername("fred@gmail.com");
        user.setFullName("Fred Tester");
        user.setEnabled(true);
        userRepository.save(user);

        Post post = new Post();
        post.setContent("Post content");
        post.setUser(user);
        postRepository.save(post);

        Comment comment = new Comment();
        comment.setContent("Nice post");
        comment.setUser(user);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        Comment foundComment = commentRepository
                .findById(savedComment.getId())
                .orElseThrow();

        assertEquals(user.getId(), foundComment.getUser().getId());
        assertEquals(post.getId(), foundComment.getPost().getId());
    }

    @Test
    public void commentGetsDeleted() {
        User user = new User();
        user.setUsername("fred@gmail.com");
        user.setFullName("Fred Tester");
        user.setEnabled(true);
        userRepository.save(user);

        Post post = new Post();
        post.setContent("Post");
        post.setUser(user);
        postRepository.save(post);

        Comment comment = new Comment();
        comment.setContent("Delete me");
        comment.setUser(user);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        commentRepository.deleteById(savedComment.getId());

        assertFalse(commentRepository.existsById(savedComment.getId()));
    }

    @Test
    public void postCanHaveMultipleComments() {
        User user = new User();
        user.setUsername("fred@gmail.com");
        user.setFullName("Fred Tester");
        user.setEnabled(true);
        userRepository.save(user);

        Post post = new Post();
        post.setContent("Post");
        post.setUser(user);
        postRepository.save(post);

        Comment firstComment = new Comment("First comment", user, post);
        Comment secondComment = new Comment("Second comment", user, post);

        commentRepository.save(firstComment);
        commentRepository.save(secondComment);

        assertEquals(2, commentRepository.count());
    }
}
