CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(250) NOT NULL,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,

    CONSTRAINT fk_comments_user
        FOREIGN KEY (user_id)
            REFERENCES users(id),

    CONSTRAINT fk_comments_posts
        FOREIGN KEY (post_id)
            REFERENCES posts(id)
);
