package org.example.testetecniconimble.form;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CobrancaForm(
    @NotBlank(message = "CPF de origem não pode ser vazio")
    String cpfOrigem,

    @NotBlank(message = "CPF do destinatário não pode ser vazio")
    String cpfDestinatario,

    @NotNull(message = "Valor não pode ser nulo")
    @DecimalMin(value = "0.01", inclusive = true, message = "Valor deve ser positivo")
    BigDecimal valor,

    String descricao){
}
