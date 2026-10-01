CREATE TABLE IF NOT EXISTS users
(
    id            UUID PRIMARY KEY,
    email         VARCHAR(255)             NOT NULL UNIQUE,
    username      VARCHAR(64)              NOT NULL,
    role          VARCHAR(255)             NOT NULL,
    password_hash VARCHAR(255)             NOT NULL,
    registered    BOOLEAN                  NOT NULL DEFAULT FALSE,
    enabled       BOOLEAN                  NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);