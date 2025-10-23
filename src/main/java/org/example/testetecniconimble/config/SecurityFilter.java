package org.example.testetecniconimble.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.testetecniconimble.entity.Usuario;
import org.example.testetecniconimble.exception.AuthorizationException;
import org.example.testetecniconimble.repository.UsuarioRepository;
import org.example.testetecniconimble.service.TokenService;
import org.example.testetecniconimble.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;

        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recoverToken(request);

        if(token !=null){
            try {
                String login = tokenService.validarToken(token);
                Usuario user = usuarioRepository.findByEmail(login).orElseThrow();
                var authentication = new UsernamePasswordAuthenticationToken(user, null ,user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (AuthorizationException e) {
                try {
                    throw new AuthorizationException("Problema ao fazer a autenticação!");
                } catch (AuthorizationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        filterChain.doFilter(request,response);

    }

    private String recoverToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token == null) return null;
        return token.replace("Bearer ", "");
    }
}
