package org.example.testetecniconimble.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.testetecniconimble.exception.AutorizadorExternoException;
import org.example.testetecniconimble.exception.CobrancaExceptional;
import org.example.testetecniconimble.form.PagamentoCartaoForm;
import org.example.testetecniconimble.service.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PutMapping(path = "saldo")
    @Operation(
            summary = "Realiza pagamento com saldo",
            description = "Permite que o pagador quite uma cobrança utilizando o saldo disponível em conta.",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "Token JWT do usuário autenticado",
                            required = true,
                            in = ParameterIn.HEADER,
                            example = "Bearer eyJhbGciOiJIUzI1NiIs..."
                    ),
                    @Parameter(
                            name = "idCobranca",
                            description = "ID da cobrança a ser paga",
                            required = true,
                            in = ParameterIn.QUERY,
                            example = "123"
                    ),
                    @Parameter(
                            name = "cpfPagador",
                            description = "CPF do pagador que realizará o pagamento",
                            required = true,
                            in = ParameterIn.QUERY,
                            example = "12345678900"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pagamento realizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou saldo insuficiente", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Cobrança não encontrada", content = @Content)
            }
    )
    public ResponseEntity<Void> realizarPagamentoComSaldo(
                                                      @RequestParam("idCobranca") Long idCobranca,
                                                      @RequestParam("cpfPagador") String cpfPagador)
            throws CobrancaExceptional {
        pagamentoService.realizarPagamentoComSaldo(idCobranca, cpfPagador);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "credito")
    @Operation(
            summary = "Realiza pagamento com crédito",
            description = "Permite que o pagador quite uma cobrança utilizando crédito disponível.",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "Token JWT do usuário autenticado",
                            required = true,
                            in = ParameterIn.HEADER,
                            example = "Bearer eyJhbGciOiJIUzI1NiIs..."
                    ),
                    @Parameter(
                            name = "idCobranca",
                            description = "ID da cobrança a ser paga",
                            required = true,
                            in = ParameterIn.QUERY,
                            example = "123"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pagamento realizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou crédito insuficiente", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Cobrança não encontrada", content = @Content)
            }
    )
    public ResponseEntity<Void> realizarPagamentoComCredito(@RequestParam("idCobranca") Long idCobranca,
                                                            @RequestBody PagamentoCartaoForm form)
            throws CobrancaExceptional, AutorizadorExternoException {
        pagamentoService.realizarPagamentoComCartao(idCobranca, form);
        return ResponseEntity.ok().build();
    }
}
