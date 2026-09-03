package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    List<User> findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrHandleContainingIgnoreCase(
        String username,
        String fullname,
        String handle
    );

    Optional<User> findUserByHandle(String handle);
}
