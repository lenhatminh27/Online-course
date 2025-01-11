CREATE TABLE accounts
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    email         VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255)        NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE roles
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE account_roles
(
    account_id INT NOT NULL,
    role_id    INT NOT NULL,
    PRIMARY KEY (account_id, role_id),
    FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- Insert roles into the 'role' table
INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('INSTRUCTOR'),
       ('LEARNER');

-- Insert one account into the 'account' table
INSERT INTO accounts (email, password_hash)
VALUES ('user@example.com', 'hashedpassword');
