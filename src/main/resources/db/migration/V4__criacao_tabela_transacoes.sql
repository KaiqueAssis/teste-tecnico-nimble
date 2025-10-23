CREATE TABLE transacao (

    id BIGSERIAL PRIMARY KEY,
    conta_origem_id BIGINT,
    conta_destino_id BIGINT NOT NULL,

    valor BIGINT NOT NULL,
    data_criacao TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

    meio_pagamento VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    tipo VARCHAR(50) NOT NULL,

    CONSTRAINT fk_conta_pagadora
        FOREIGN KEY (conta_origem_id)
        REFERENCES conta (id)
        ON DELETE RESTRICT,


    CONSTRAINT fk_conta_recebedora
        FOREIGN KEY (conta_destino_id)
        REFERENCES conta (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_transacao_origem_id ON transacao (conta_origem_id);
CREATE INDEX idx_transacao_destino_id ON transacao (conta_destino_id);