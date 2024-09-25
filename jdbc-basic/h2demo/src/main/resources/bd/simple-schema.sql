CREATE TABLE usuarios (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE autoridades (
    authority VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES usuarios(username) ON DELETE CASCADE,
    PRIMARY KEY (authority, username)
);
