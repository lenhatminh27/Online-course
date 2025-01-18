
-- Insert roles into the 'role' table
INSERT INTO roles (name) VALUES ('ADMIN'),
                                ('INSTRUCTOR'),
                                ('LEARNER');

-- Insert one account into the 'account' table password_hash: 12345
INSERT INTO accounts (email, password_hash)
VALUES ('admin@gmail.com', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5');


INSERT INTO accounts (email, password_hash)
VALUES ('instuctor@gmail.com', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5');

INSERT INTO accounts (email, password_hash)
VALUES ('learner@gmail.com', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5');

INSERT INTO account_roles (account_id, role_id)
VALUES (1, 1);

INSERT INTO account_roles (account_id, role_id)
VALUES (2, 2);

INSERT INTO account_roles (account_id, role_id)
VALUES (3, 3);

INSERT INTO permissions (name)
VALUES
    ('UPLOAD_FILE'),
    ('DOWNLOAD_FILE'),
    ('CREATE_BLOG'),
    ('UPDATE_BLOG'),
    ('DELETE_BLOG'),
    ('REACT_BLOG'),
    ('CREATE_COMMENT_BLOG'),
    ('UPDATE_COMMENT_BLOG'),
    ('DELETE_COMMENT_BLOG'),
    ('ANSWER_COMMENT_BLOG'),
    ('BOOKMARK_BLOG');

INSERT INTO role_permission (role_id, permission_id)
VALUES (1, 1);

INSERT INTO account_profile (id, first_name, last_name, created_at, updated_at)
VALUES
    (1, 'admin', 'nguyen', NOW(), NOW()),
    (2, 'instructor', 'nguyen', NOW(), NOW()),
    (3, 'learner', 'nguyen', NOW(), NOW());
