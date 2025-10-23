package org.example.testetecniconimble.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.testetecniconimble.entity.enums.MeioDePagamento;
import org.example.testetecniconimble.entity.enums.StatusTransacao;
import org.example.testetecniconimble.entity.enums.TipoDeTransacao;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "transacao")
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conta_origem_id")
    private Conta contaPagadora;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id")
    private Conta contaRecebedora;

    @Column(name = "valor")
    private BigInteger valor;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "meio_pagamento")
    private MeioDePagamento meioDePagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusTransacao statusTransacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoDeTransacao tipoDeTransacao;

    public Transacao(Conta contaPagadora, Conta contaRecebedora,
                     BigInteger valor, MeioDePagamento meioDePagamento,
                     TipoDeTransacao tipoDeTransacao, StatusTransacao statusTransacao) {
        this.contaPagadora = contaPagadora;
        this.contaRecebedora = contaRecebedora;
        this.valor = valor;
        this.meioDePagamento = meioDePagamento;
        this.tipoDeTransacao = tipoDeTransacao;
        this.statusTransacao = statusTransacao;

    }

    public Transacao() {

    }
}
