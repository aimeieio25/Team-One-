\set ON_ERROR_STOP on

SELECT EXISTS (
    SELECT 1
    FROM users
    WHERE id = 4
) AS target_exists \gset

\if :target_exists
\else
    \echo 'User ID 4 does not exist.'
    \echo 'Create or log in as the new user before running this script.'
    \quit
\endif

BEGIN;

INSERT INTO friend_requests (sender_id, receiver_id, status)
SELECT sender.id, receiver.id, 'pending'
FROM users AS sender
CROSS JOIN users AS receiver
WHERE sender.username IN (
    'bwayne@example.com',
    'pquill@example.com',
    'sbob@example.com'
)
  AND receiver.id = 4
  AND sender.id <> receiver.id
  AND NOT EXISTS (
      SELECT 1
      FROM friend_requests AS existing
      WHERE existing.sender_id = sender.id
        AND existing.receiver_id = receiver.id
  );

COMMIT;

SELECT
    sender.full_name AS sender,
    receiver.full_name AS receiver,
    request.status
FROM friend_requests AS request
JOIN users AS sender ON sender.id = request.sender_id
JOIN users AS receiver ON receiver.id = request.receiver_id
WHERE receiver.id = 4
ORDER BY sender.full_name;
