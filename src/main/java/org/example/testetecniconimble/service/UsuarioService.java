package org.example.testetecniconimble.service;

import org.example.testetecniconimble.entity.Usuario;
import org.example.testetecniconimble.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final ContaService contaService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository repository, ContaService contaService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.contaService = contaService;
        this.passwordEncoder = passwordEncoder;
    }


    public void cadastrar(String email, String senhaPura, String cpf, String nome) {
        if (repository.findByEmail(email).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado!");
        }

        if (repository.findByCpf(cpf).isPresent()) {
            throw new RuntimeException("Cpf já cadastrado!");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(passwordEncoder.encode(senhaPura));
        novoUsuario.setCpf(cpf);
        novoUsuario.setNome(nome);
        repository.save(novoUsuario);

        contaService.criarConta(novoUsuario);
    }

    public Usuario buscarUsuarioPorCpf(String cpf) {
        return repository.findByCpf(cpf).orElseThrow();
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return repository.findByEmail(email).orElseThrow();
    }
}