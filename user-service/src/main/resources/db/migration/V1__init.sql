CREATE TABLE roles (
    id UUID PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    user_name VARCHAR(255),
    user_surname VARCHAR(255),
    user_email VARCHAR(255) UNIQUE,
    user_password VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    registration_date TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
