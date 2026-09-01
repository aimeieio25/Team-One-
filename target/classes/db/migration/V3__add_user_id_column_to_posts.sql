ALTER TABLE posts
    ADD COLUMN user_id bigint,
    ADD CONSTRAINT fk_posts_user
        FOREIGN KEY (user_id)
        REFERENCES users(id);