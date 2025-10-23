package org.example.testetecniconimble.form;

import jakarta.validation.constraints.NotBlank;

public record PagamentoCartaoForm(
    @NotBlank(message = "O numero do cartão tem que ser passado")
    String numeroCartao,
    @NotBlank(message = "O cvv do cartão tem que ser passado")
    String cvv,
    @NotBlank(message = "O numero do cartão tem que ser passado")
    String dataExpiracao){
}
