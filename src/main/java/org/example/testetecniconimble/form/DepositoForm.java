package org.example.testetecniconimble.form;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositoForm(
                           @NotNull(message = "Valor não pode ser nulo")
                           @DecimalMin(value = "0.01", inclusive = true, message = "Valor deve ser positivo")
                           BigDecimal valor,
                           @NotBlank(message = "O cpf tem que ser passado")
                           String cpf) {
}
