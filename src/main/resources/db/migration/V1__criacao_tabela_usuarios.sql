CREATE TABLE usuario (
        id BIGSERIAL PRIMARY KEY,
        cpf VARCHAR(14) UNIQUE NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL,
        senha VARCHAR(255) NOT NULL,
        nome VARCHAR(100) NOT NULL
);

CREATE INDEX idx_usuario_cpf ON usuario (cpf);
CREATE INDEX idx_usuario_email ON usuario (email);