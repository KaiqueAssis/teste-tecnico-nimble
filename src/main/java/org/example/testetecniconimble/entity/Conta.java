package org.example.testetecniconimble.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.testetecniconimble.dto.ContaDto;
import org.example.testetecniconimble.utils.ConversorMonetario;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "usuario_id")
    @OneToOne
    private Usuario usuario;
    @JoinColumn(name = "saldo")
    private BigInteger saldo;

    @Column(name = "ult_atu", nullable = false)
    private LocalDateTime ultimaAtualizacao;

    public Conta(Usuario usuario) {
        this.usuario = usuario;
        this.saldo = BigInteger.ZERO;
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    public Conta() {

    }

    public ContaDto converterEmDto(){
        return new ContaDto(this.usuario.getNome(), ConversorMonetario.centavosParaReais(this.saldo),
                this.ultimaAtualizacao);
    }
}
