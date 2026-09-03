package com.makersacademy.acebook.model;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    private User user = new User();

    @Test
    public void userContainsUsername() {
        user.setUsername("fred@gmail.com");
        assertThat(user.getUsername(), containsString("fred@gmail.com"));
    }

    @Test
    public void userContainsFullName() {
        user.setFullName("Fred Tester");
        assertThat(user.getFullName(), containsString("Fred Tester"));
    }

    @Test
    public void usernameCanBeChanged() {
        user.setUsername("fred@gmail.com");
        user.setUsername("john@gmail.com");

        assertEquals("john@gmail.com", user.getUsername());
    }

    @Test
    public void fullNameCanBeChanged() {
        user.setFullName("Fred Tester");
        user.setFullName("John Smith");

        assertEquals("John Smith", user.getFullName());
    }

    @Test
    public void userCanBeCreatedWithUsernameAndFullName() {
        User user = new User("fred@gmail.com", "Fred Tester");

        assertEquals("fred@gmail.com", user.getUsername());
        assertEquals("Fred Tester", user.getFullName());
    }
}
