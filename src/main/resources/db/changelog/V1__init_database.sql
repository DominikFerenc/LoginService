CREATE TABLE IF NOT EXISTS users_table (
    id SERIAL PRIMARY KEY,
    login CHAR(255) NOT NULL,
    request_count NUMERIC
)