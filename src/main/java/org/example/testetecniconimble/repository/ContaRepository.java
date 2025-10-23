package org.example.testetecniconimble.repository;

import org.example.testetecniconimble.entity.Conta;
import org.example.testetecniconimble.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByUsuario(Usuario usuario);

    Optional<Conta> findByUsuario_Cpf(String cpf);
}
