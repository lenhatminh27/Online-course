CREATE TABLE accounts (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          password_hash VARCHAR(255) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE refresh_token (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               refresh_token MEDIUMTEXT NOT NULL,
                               revoked BOOLEAN DEFAULT FALSE,
                               account_id BIGINT,
                               FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL
);

CREATE TABLE permissions (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(50) NOT NULL
);

CREATE TABLE role_permission (
                                 role_id BIGINT NOT NULL,
                                 permission_id BIGINT NOT NULL,
                                 PRIMARY KEY (role_id, permission_id),
                                 FOREIGN KEY (role_id) REFERENCES roles(id),
                                 FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

CREATE TABLE account_roles (
                               account_id BIGINT NOT NULL,
                               role_id BIGINT NOT NULL,
                               PRIMARY KEY (account_id, role_id),
                               FOREIGN KEY (account_id) REFERENCES accounts(id),
                               FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Insert roles into the 'role' table
INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('INSTRUCTOR'),
       ('LEARNER');

-- Insert one account into the 'account' table
INSERT INTO accounts (email, password_hash)
VALUES ('admin@gmail.com', '12345');


INSERT INTO accounts (email, password_hash)
VALUES ('instuctor@gmail.com', '12345');

INSERT INTO accounts (email, password_hash)
VALUES ('leaner@gmail.com', '12345');

INSERT INTO account_roles (account_id, role_id)
VALUES (1, 1);

INSERT INTO account_roles (account_id, role_id)
VALUES (2, 2);

INSERT INTO account_roles (account_id, role_id)
VALUES (3, 3);

INSERT INTO permissions (name)
VALUES
    ('AUTHORIZATION_MANAGEMENT'),
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
