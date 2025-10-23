package org.example.testetecniconimble.service;

import org.example.testetecniconimble.entity.Usuario;
import org.example.testetecniconimble.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository repository;

    public AutenticacaoService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        if (login.contains("@")) {
            Optional<Usuario> usuarioPeloEmail = repository.findByEmail(login);

            if (usuarioPeloEmail.isPresent()) {
                return usuarioPeloEmail.get();
            }
        }
        if (login.length() == 14 && !login.contains("@")) {
            Optional<Usuario> usuarioPeloCpf = repository.findByCpf(login);

            if (usuarioPeloCpf.isPresent()) {
                return usuarioPeloCpf.get();
            }
        }

        throw new UsernameNotFoundException("Usuário não encontrado com o pelo login: " + login);
    }
}

