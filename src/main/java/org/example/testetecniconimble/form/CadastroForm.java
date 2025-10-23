package org.example.testetecniconimble.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CadastroForm(@NotBlank(message = "O nome é obrigatório.")
                          String nome,

                           @NotBlank(message = "O e-mail é obrigatório.")
                          @Email(message = "Formato de e-mail inválido.")
                          String email,

                           @NotBlank(message = "A senha é obrigatória.")
                          String senha,
                           @NotBlank(message = "O CPF é obrigatório.")
                          @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$|^\\d{11}$",
                                  message = "CPF inválido. Use o formato '999.999.999-99' ou '11 dígitos'.")
                          String cpf) {
}
