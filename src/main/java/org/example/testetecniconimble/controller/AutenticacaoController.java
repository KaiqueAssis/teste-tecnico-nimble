package org.example.testetecniconimble.controller;

import org.example.testetecniconimble.dto.LoginDto;
import org.example.testetecniconimble.dto.TokenDto;
import org.example.testetecniconimble.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {


    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager manager, TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<TokenDto> efetuarLogin(@RequestBody LoginDto dto) {
        try {
            var tokenAutenticacao = new UsernamePasswordAuthenticationToken(
                    dto.login(),
                    dto.senha()
            );

            var authentication = manager.authenticate(tokenAutenticacao);


            String token = tokenService.gerarToken(authentication);

            return ResponseEntity.ok().body(new TokenDto(token));

        } catch (Exception e) {
            System.err.println("Falha na autenticação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}