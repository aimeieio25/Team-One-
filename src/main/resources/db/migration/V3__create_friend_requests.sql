CREATE TABLE friend_requests (
    id BIGSERIAL PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',

    CONSTRAINT fk_friend_request_sender
        FOREIGN KEY (sender_id)
        REFERENCES users(id),

    CONSTRAINT fk_friend_request_receiver
        FOREIGN KEY (receiver_id)
        REFERENCES users(id)
);

INSERT INTO users (username, full_name, enabled)
VALUES
    ('bwayne@example.com', 'Bruce Wayne', TRUE),
    ('pquill@example.com', 'Peter Quill', TRUE),
    ('sbob@example.com', 'SpongeBob SquarePants', TRUE);