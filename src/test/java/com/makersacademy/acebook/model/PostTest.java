package com.makersacademy.acebook.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PostTest {

	private Post post = new Post("hello");

	@Test
	public void postHasContent() {
		assertThat(post.getContent(), containsString("hello"));
	}

	@Test
	public void postContentCanBeChanged() {
		Post post = new Post("Old content");

		post.setContent("New content");

		assertEquals("New content", post.getContent());
	}

	@Test
	public void postCanBelongToUser() {
		User user = new User("test@example.com", "Test User");
		Post post = new Post("Hello");

		post.setUser(user);

		assertEquals(user, post.getUser());
		assertEquals("test@example.com", post.getUser().getUsername());
	}


}
