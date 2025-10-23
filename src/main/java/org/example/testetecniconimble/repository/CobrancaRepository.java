package org.example.testetecniconimble.repository;

import org.example.testetecniconimble.entity.Cobranca;
import org.example.testetecniconimble.entity.enums.StatusCobranca;
import org.example.testetecniconimble.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CobrancaRepository extends JpaRepository<Cobranca, Integer> {

    @Query("""
    SELECT c FROM Cobranca c
    WHERE c.originador = :originador
    AND (:status IS NULL OR c.status = :status)
    ORDER BY c.dataCriacao desc
""")
    List<Cobranca> listarCobrancaFeitasPeloOriginador(
            @Param("originador") Usuario originador,
            @Param("status") StatusCobranca statusCobranca
    );

    @Query("""
    SELECT c FROM Cobranca c
    WHERE c.destinatario = :destinatario
    AND (:status IS NULL OR c.status = :status)
    ORDER BY c.dataCriacao desc
""")
    List<Cobranca> listarCobrancaRecebidas(
            @Param("destinatario") Usuario destinatario,
            @Param("status") StatusCobranca statusCobranca
    );

    Cobranca findById(long id);


}
