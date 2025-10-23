package org.example.testetecniconimble.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.testetecniconimble.dto.ContaDto;
import org.example.testetecniconimble.exception.ContaNaoEncontradaExceptional;
import org.example.testetecniconimble.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "contas")
public class ContaController {

    private ContaService contaService;

    public ContaController(final ContaService contaService) {
        this.contaService = contaService;
    }

    @Operation(
            summary = "Busca conta pelos dados do CPF",
            description = "Retorna os detalhes da conta do usuário associado ao CPF fornecido.",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "Token de autenticação (Bearer Token) do usuário logado.",
                            required = true,
                            in = io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER
                    ),
                    @Parameter(
                            name = "cpf",
                            description = "CPF do usuário para buscar a conta.",
                            required = true,
                            example = "12345678900",
                            in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Busca realizada com sucesso. Retorna os dados da conta.",
                            content = @Content(schema = @Schema(implementation = ContaDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Não autorizado. Token de autenticação ausente ou inválido."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada para o CPF informado (ContaNaoEncontradaExceptional)."
                    )
            }
    )
    @GetMapping
    public ResponseEntity<ContaDto> buscarContaPorCpf(
                                                      @RequestParam("cpf") String cpf) throws ContaNaoEncontradaExceptional {
        return ResponseEntity.ok(contaService.buscarContaPorCpfUser(cpf));
    }
}
