INSERT INTO users (username, full_name, enabled)
VALUES
    ('bwayne@example.com', 'Bruce Wayne', TRUE),
    ('pquill@example.com', 'Peter Quill', TRUE),
    ('sbob@example.com', 'SpongeBob SquarePants', TRUE);


INSERT INTO posts (content, user_id)
VALUES
    ('This is SpongeBob!', 3),
    ('I am not batman', 1);

