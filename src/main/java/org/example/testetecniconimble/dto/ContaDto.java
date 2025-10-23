package org.example.testetecniconimble.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ContaDto(String nomeUsuario, BigDecimal saldo, LocalDateTime ultimaAtualizacao) {
}
