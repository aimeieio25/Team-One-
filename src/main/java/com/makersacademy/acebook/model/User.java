package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;

import static java.lang.Boolean.TRUE;

@Data
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String fullName;
    private boolean enabled;
    private String profilePicture;
    private String handle;

    public User() {
        this.enabled = TRUE;
    }

    public User(String username) {
        this.username = username;
        this.enabled = TRUE;
    }

    public User(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
        this.enabled = TRUE;
    }

    public User(String username, boolean enabled) {
        this.username = username;
        this.enabled = enabled;
    }
}
