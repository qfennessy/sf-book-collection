-- Check if roles exist
SELECT * FROM roles;

-- Insert roles manually if needed 
INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER') ON CONFLICT DO NOTHING;
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ADMIN') ON CONFLICT DO NOTHING;

-- Check users
SELECT * FROM users;

-- Check user roles
SELECT u.username, r.name 
FROM users u 
JOIN user_roles ur ON u.id = ur.user_id 
JOIN roles r ON r.id = ur.role_id;