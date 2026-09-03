package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import java.util.List;

@Data
@Entity
@Table(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
    @OneToMany(mappedBy = "post")
    private List<PostReaction> reactions;

    public Post() {
    }

    public Post(String content) {
        this.content = content;
    }

    public Post(String content, User user) {
        this.content = content;
        this.user = user;
    }

    public long getLikeCount() {

        if (reactions == null) {
            return 0;
        }

        return reactions.stream()
                .filter(reaction -> reaction.getReaction().equals("LIKE"))
                .count();
    }

    public long getDislikeCount() {

        if (reactions == null) {
            return 0;
        }

        return reactions.stream()
                .filter(reaction -> reaction.getReaction().equals("DISLIKE"))
                .count();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}