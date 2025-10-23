package org.example.testetecniconimble.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.testetecniconimble.entity.enums.MeioDePagamento;
import org.example.testetecniconimble.entity.enums.StatusCobranca;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "cobranca")
public class Cobranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "originador_id")
    private Conta originador;

    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private Conta destinatario;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor", nullable = false)
    private BigInteger valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusCobranca status;

    @Enumerated(EnumType.STRING)
    @Column(name = "meio_de_pagamento")
    private MeioDePagamento meioDePagamento;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    public Cobranca(Conta originador, Conta destinatario, String descricao, BigInteger valor) {
        this.originador = originador;
        this.destinatario = destinatario;
        this.descricao = descricao;
        this.valor = valor;
        this.status = StatusCobranca.PENDENTE;
    }

    public Cobranca() {

    }
}
