package org.example.testetecniconimble.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.example.testetecniconimble.exception.AutorizadorExternoException;
import org.example.testetecniconimble.exception.ContaNaoEncontradaExceptional;
import org.example.testetecniconimble.exception.DepositoException;
import org.example.testetecniconimble.form.DepositoForm;
import org.example.testetecniconimble.service.DepositoService;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "deposito")
public class DepositoController {

    private final DepositoService depositoService;

    public DepositoController(DepositoService depositoService) {
        this.depositoService = depositoService;
    }
    @Operation(
            summary = "Realiza um Depósito em uma Conta",
            description = "Processa uma transação de depósito. Requer autorização externa e atualiza o saldo da conta destino.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Depósito realizado com sucesso. (No Content)"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requisição inválida (Erro de validação @Valid)."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta de destino não encontrada (ContaNaoEncontradaExceptional)."
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Falha de comunicação com o autorizador externo (AutorizadorExternoException)."
                    )
            }
    )
    @PostMapping()
    public ResponseEntity<Void> realizarDeposito(@Valid @RequestBody DepositoForm form)
            throws AutorizadorExternoException, DepositoException, ContaNaoEncontradaExceptional {
        depositoService.realizarDeposito(form);
        return ResponseEntity.ok().build();
    }
}
