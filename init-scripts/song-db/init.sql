CREATE TABLE song (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    artist VARCHAR(255) NOT NULL,
    album VARCHAR(255),
    duration VARCHAR(50),
    year VARCHAR(4),
    resource_id BIGINT
);
