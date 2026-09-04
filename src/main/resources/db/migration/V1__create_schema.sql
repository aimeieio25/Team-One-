CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(255),
    profile_picture VARCHAR(500),
    enabled BOOLEAN NOT NULL,
    handle VARCHAR(50) UNIQUE
);

CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(250) NOT NULL,
    user_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_posts_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE TABLE friend_requests (
    id BIGSERIAL PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',

    CONSTRAINT fk_friend_requests_sender
        FOREIGN KEY (sender_id)
        REFERENCES users(id),

    CONSTRAINT fk_friend_requests_receiver
        FOREIGN KEY (receiver_id)
        REFERENCES users(id)
);

CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(250) NOT NULL,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,

    CONSTRAINT fk_comments_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_comments_post
        FOREIGN KEY (post_id)
        REFERENCES posts(id)
        ON DELETE CASCADE
);

CREATE TABLE post_reactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    reaction VARCHAR(10) NOT NULL,

    CONSTRAINT fk_post_reactions_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_post_reactions_post
        FOREIGN KEY (post_id)
        REFERENCES posts(id)
        ON DELETE CASCADE,

    CONSTRAINT unique_user_post_reaction
        UNIQUE (user_id, post_id)
);

CREATE INDEX idx_posts_user_id ON posts(user_id);
CREATE INDEX idx_friend_requests_sender_id ON friend_requests(sender_id);
CREATE INDEX idx_friend_requests_receiver_id ON friend_requests(receiver_id);
CREATE INDEX idx_comments_user_id ON comments(user_id);
CREATE INDEX idx_comments_post_id ON comments(post_id);
CREATE INDEX idx_post_reactions_user_id ON post_reactions(user_id);
CREATE INDEX idx_post_reactions_post_id ON post_reactions(post_id);
