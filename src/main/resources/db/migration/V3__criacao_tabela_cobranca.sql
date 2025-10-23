CREATE TABLE cobranca (

        id BIGSERIAL PRIMARY KEY,
        originador_id BIGINT NOT NULL,
        destinatario_id BIGINT NOT NULL,
        descricao VARCHAR(255),
        valor BIGINT NOT NULL,
        status VARCHAR(50) NOT NULL,
        meio_de_pagamento VARCHAR(50),
        data_criacao TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
        data_pagamento TIMESTAMP WITHOUT TIME ZONE,

        CONSTRAINT fk_originador
            FOREIGN KEY (originador_id)
                REFERENCES conta (id)
                    ON DELETE RESTRICT,

        CONSTRAINT fk_destinatario
            FOREIGN KEY (destinatario_id)
                REFERENCES conta (id)
                    ON DELETE RESTRICT
);

CREATE INDEX idx_cobranca_originador_id ON cobranca (originador_id);
CREATE INDEX idx_cobranca_destinatario_id ON cobranca (destinatario_id);