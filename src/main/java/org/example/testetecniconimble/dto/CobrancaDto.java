package org.example.testetecniconimble.dto;

import lombok.Getter;
import org.example.testetecniconimble.entity.Cobranca;
import org.example.testetecniconimble.utils.ConversorMonetario;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
public class CobrancaDto {

    private final String nomeDestinatario;
    private final BigDecimal valor;
    private final LocalDateTime dataCriacao;
    private final String descricao;

    public CobrancaDto(Cobranca cobranca) {
        this.nomeDestinatario = cobranca.getDestinatario().getUsuario().getNome();
        this.valor = ConversorMonetario.centavosParaReais(cobranca.getValor());
        this.dataCriacao = cobranca.getDataCriacao();
        this.descricao = cobranca.getDescricao();
    }
}
