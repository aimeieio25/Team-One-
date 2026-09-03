ALTER TABLE users
    ADD COLUMN handle VARCHAR(50);

UPDATE users
SET handle = SPLIT_PART(username, '@', 1);

ALTER TABLE users
    ADD CONSTRAINT users_handle_unique UNIQUE (handle);