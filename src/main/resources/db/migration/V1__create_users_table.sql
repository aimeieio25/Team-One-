CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(255),
    profile_picture VARCHAR(500),
    enabled BOOLEAN NOT NULL
);