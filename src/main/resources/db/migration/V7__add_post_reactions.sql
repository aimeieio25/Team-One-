CREATE TABLE post_reactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    reaction VARCHAR(10) NOT NULL,

    CONSTRAINT fk_post_reaction_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_post_reaction_post
        FOREIGN KEY (post_id)
        REFERENCES posts(id)
        ON DELETE CASCADE,

    CONSTRAINT unique_user_post_reaction
        UNIQUE (user_id, post_id)
);