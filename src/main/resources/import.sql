-- Default roles
INSERT INTO roles (name) VALUES ('ROLE_USER') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;

-- Default admin user (username: admin, password: admin123)
INSERT INTO users (username, email, password) 
VALUES ('admin', 'admin@sfcollection.com', '$2a$10$hIpaOr0dH.jnSMkQefoRQuX9nbJ7Zj7tOXGm3d6vK.u0T/lc2AqEO') 
ON CONFLICT DO NOTHING;

-- Assign admin role to admin user
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;