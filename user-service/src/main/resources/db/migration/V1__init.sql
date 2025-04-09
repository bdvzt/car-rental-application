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
    is_active BOOLEAN NOT NULL,
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE refreshtoken (
    id UUID PRIMARY KEY,
    user_id UUID UNIQUE,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
