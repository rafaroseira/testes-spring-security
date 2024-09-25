INSERT INTO usuarios (username, password, enabled) VALUES
('usuario1', '{noop}senha1', TRUE),
('usuario2', '{noop}senha2', TRUE);

INSERT INTO autoridades (authority, username) VALUES
('ROLE_USER', 'usuario1'),
('ROLE_USER', 'usuario2');