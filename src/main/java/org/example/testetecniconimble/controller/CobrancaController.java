package org.example.testetecniconimble.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.testetecniconimble.dto.CobrancaDto;
import org.example.testetecniconimble.entity.enums.StatusCobranca;
import org.example.testetecniconimble.exception.AuthorizationException;
import org.example.testetecniconimble.exception.AutorizadorExternoException;
import org.example.testetecniconimble.exception.CancelamentoException;
import org.example.testetecniconimble.exception.CobrancaExceptional;
import org.example.testetecniconimble.form.CobrancaForm;
import org.example.testetecniconimble.service.CancelamentoService;
import org.example.testetecniconimble.service.CobrancaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cobranca")
@Tag(name = "Cobranças", description = "Endpoints relacionados às cobranças criadas, recebidas e ao cancelamento das cobranças")
public class CobrancaController {

    private final CobrancaService cobrancaService;
    private final CancelamentoService cancelamentoService;

    public CobrancaController(CobrancaService cobrancaService, CancelamentoService cancelamentoService) {
        this.cobrancaService = cobrancaService;
        this.cancelamentoService = cancelamentoService;
    }


    @GetMapping(path = "listar-cobrancas-criadas/")
    @Operation(
            summary = "Listar cobranças criadas pelo usuário",
            description = "Retorna todas as cobranças criadas por um usuário específico, filtradas por status.",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "Token JWT do usuário autenticado",
                            required = true,
                            in = ParameterIn.HEADER,
                            example = "Bearer eyJhbGciOiJIUzI1NiIs..."
                    ),
                    @Parameter(
                            name = "status",
                            description = "status cobrança",
                            required = true,
                            in = ParameterIn.QUERY,
                            example = "PENDENTE"
                    ),
                    @Parameter(
                            name = "cpfUsuario",
                            description = "Cpf de identificação do usuário",
                            required = true,
                            in = ParameterIn.QUERY,
                            example = "111.111.111-21"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cobranças listadas com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CobrancaDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autorizado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
            }
    )
    public ResponseEntity<List<CobrancaDto>> listarCobrancasCriadasPeloUsuario(
                                                                               @RequestParam("cpfUsuario")
                                                                               String  cpfUsuario,
                                                                               @RequestParam("status")
                                                                               StatusCobranca statusCobranca) {
        return ResponseEntity.ok(cobrancaService.listarCobrancasCriadasPeloUsuario(cpfUsuario, statusCobranca));
    }

    @GetMapping(path = "teste")
    public ResponseEntity<Void> teste() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "listar-cobrancas-recebidas/")
    @Operation(
            summary = "Listar cobranças criadas pelo usuário",
            description = "Retorna todas as cobranças criadas por um usuário específico, filtradas por status.",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "Token JWT do usuário autenticado",
                            required = true,
                            in = ParameterIn.HEADER,
                            example = "Bearer eyJhbGciOiJIUzI1NiIs..."
                    ),
                    @Parameter(
                            name = "status",
                            description = "status cobrança",
                            required = true,
                            in = ParameterIn.QUERY,
                            example = "PENDENTE"
                    ),
                    @Parameter(
                            name = "cpfUsuario",
                            description = "Cpf de identificação do usuário",
                            required = true,
                            in = ParameterIn.QUERY,
                            example = "111.111.111-21"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cobranças listadas com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CobrancaDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autorizado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
            }
    )
    public ResponseEntity<List<CobrancaDto>> listarCobrancasRecebidas(
                                                                      @RequestParam("cpfUsuario")
                                                                      String cpfUsuario,
                                                                      @RequestParam("status")
                                                                      StatusCobranca statusCobranca) {
        return ResponseEntity.ok(cobrancaService.listarCobrancasRecebidas(cpfUsuario, statusCobranca));
    }

    @PostMapping
    @Operation(
            summary = "Cria uma nova cobrança",
            description = "Cria uma cobrança com base nos dados informados no corpo da requisição. É necessário autenticação via token JWT.",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "Token JWT do usuário autenticado",
                            required = true,
                            in = ParameterIn.HEADER,
                            example = "Bearer eyJhbGciOiJIUzI1NiIs..."
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cobrança criada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrado"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autorizado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
            }
    )
    public ResponseEntity<Void> criarCobranca(
                                              @RequestBody @Valid CobrancaForm form) throws CobrancaExceptional {
        cobrancaService.criarCobranca(form);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping(path = "cancelar")
    @Operation(
            summary = "Cancela uma cobrança existente",
            description = "Permite o cancelamento de uma cobrança a partir do seu ID da cobrança e CPF do usuário. É necessário autenticação via token JWT.",
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
                            description = "Id da cobrança",
                            required = true,
                            in = ParameterIn.QUERY,
                            example = "11"
                    ),
                    @Parameter(
                            name = "cpf",
                            description = "Cpf de identificação do usuário",
                            required = true,
                            in = ParameterIn.QUERY,
                            example = "11"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cobrança cancelada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autorizado", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Cobrança não encontrada", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
            }
    )
    public ResponseEntity<Void> cancelarCobranca(@RequestParam("idCobranca") Long idCobranca,
                                                 @RequestParam("cpf") String cpf)
            throws AutorizadorExternoException, AuthorizationException, CancelamentoException, CobrancaExceptional {
        cancelamentoService.cancelarCobrancaPorId(idCobranca, cpf);
        return ResponseEntity.ok().build();
    }


}
