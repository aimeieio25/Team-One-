package com.makersacademy.acebook.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CommentTest {

    private Comment comment = new Comment();

    @Test
    public void commentHasContent() {
        comment.setContent("This is a comment.");
        assertThat(comment.getContent(), containsString("This is a comment."));
    }

    @Test
    public void commentContentCanBeChanged() {
        Comment comment = new Comment();

        comment.setContent("Old comment");
        comment.setContent("New comment");

        assertEquals("New comment", comment.getContent());
    }

    @Test
    public void commentCanBelongToUser() {
        User user = new User("fred@gmail.com", "Fred Tester");
        Comment comment = new Comment();
        comment.setContent("This is a comment.");

        comment.setUser(user);

        assertEquals(user, comment.getUser());
        assertEquals("fred@gmail.com", comment.getUser().getUsername());
    }
}