package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}