CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(250) NOT NULL,
    user_id BIGINT,

    CONSTRAINT fk_posts_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);