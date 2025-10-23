package org.example.testetecniconimble.controller;
import org.example.testetecniconimble.entity.Usuario;

import org.example.testetecniconimble.form.CadastroForm;
import org.example.testetecniconimble.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping(path = "cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Usuario> cadastrar(@RequestBody CadastroForm request) {
        usuarioService.cadastrar(request.email(), request.senha(),
                request.cpf(), request.nome());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}