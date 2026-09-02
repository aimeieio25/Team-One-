package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@Entity
@Table(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Sorry :/ The Post cannae be empty")
    @Size(max = 250, message = "Sorry :/ Post must be 250 characters or less")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Post() {}

    public Post(String content) {
        this.content = content;
    }

    public Post(String content, User user) {
        this.content = content;
        this.user = user;
    }
}
