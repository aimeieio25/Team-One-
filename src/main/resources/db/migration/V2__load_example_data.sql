INSERT INTO users (username, full_name, enabled, handle)
VALUES
    ('bwayne@example.com', 'Bruce Wayne', TRUE, 'bwayne'),
    ('pquill@example.com', 'Peter Quill', TRUE, 'pquill'),
    ('sbob@example.com', 'SpongeBob SquarePants', TRUE, 'sbob');

INSERT INTO posts (content, user_id)
SELECT 'This is SpongeBob!', id
FROM users
WHERE username = 'sbob@example.com';

INSERT INTO posts (content, user_id)
SELECT 'I am not batman', id
FROM users
WHERE username = 'bwayne@example.com';
