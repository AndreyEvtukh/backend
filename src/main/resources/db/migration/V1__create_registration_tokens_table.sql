-- Таблица для хранения временных данных регистрации
CREATE TABLE IF NOT EXISTS registration_tokens (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL,
    verification_code VARCHAR(6) NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Индекс для быстрого поиска по email и verification_code
CREATE INDEX IF NOT EXISTS idx_registration_tokens_email_code 
ON registration_tokens(email, verification_code);
