package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void userCanChangeFullName() {
        User user = new User("test@example.com", "Old Name");
        userRepository.save(user);

        user.setFullName("New Name");
        userRepository.save(user);

        User updatedUser = userRepository
                .findUserByUsername("test@example.com")
                .orElseThrow();

        assertEquals("New Name", updatedUser.getFullName());
    }

    @Test
    void findsUserByHandle() {
        User user = new User("test@example.com", "Test User");
        user.setHandle("testuser");

        userRepository.save(user);

        Optional<User> result = userRepository.findUserByHandle("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getHandle());
    }

    @Test
    void findsUserByPartialHandle() {
        User user = new User("pparker@example.com", "Peter Parker");
        user.setHandle("spider-man");

        userRepository.save(user);

        List<User> results =
                userRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrHandleContainingIgnoreCase(
                        "spider",
                        "spider",
                        "spider"
                );

        assertTrue(results.contains(user));
    }

    @Test
    void canDetectThatAHandleIsAlreadyTaken() {
        User user = new User("first@example.com", "First User");
        user.setHandle("taken-handle");
        userRepository.save(user);

        Optional<User> result =
                userRepository.findUserByHandle("taken-handle");

        assertTrue(result.isPresent());
        assertEquals("first@example.com", result.get().getUsername());
    }

}