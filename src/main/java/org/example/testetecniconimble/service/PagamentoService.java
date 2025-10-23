package org.example.testetecniconimble.service;

import org.example.testetecniconimble.entity.*;
import org.example.testetecniconimble.entity.enums.MeioDePagamento;
import org.example.testetecniconimble.entity.enums.StatusCobranca;
import org.example.testetecniconimble.entity.enums.StatusTransacao;
import org.example.testetecniconimble.entity.enums.TipoDeTransacao;
import org.example.testetecniconimble.exception.AutorizadorExternoException;
import org.example.testetecniconimble.exception.CobrancaExceptional;
import org.example.testetecniconimble.form.PagamentoCartaoForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class PagamentoService {

    private final CobrancaService cobrancaService;
    private final ContaService contaService;
    private final CartaoService cartaoService;
    private final AutorizadorExternoService autorizadorExternoService;
    private final TransacaoService transacaoService;

    public PagamentoService(CobrancaService cobrancaService, ContaService contaService, CartaoService cartaoService, AutorizadorExternoService autorizadorExternoService, TransacaoService transacaoService) {
        this.cobrancaService = cobrancaService;
        this.contaService = contaService;
        this.cartaoService = cartaoService;
        this.autorizadorExternoService = autorizadorExternoService;
        this.transacaoService = transacaoService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void realizarPagamentoComSaldo(Long idCobranca, String cpfPagador) throws CobrancaExceptional {
        Cobranca cobranca = cobrancaService.buscarCobrancaPorId(idCobranca);

        if (!cobranca.getDestinatario().getUsuario().getCpf().equals(cpfPagador)) {
            throw new CobrancaExceptional("Ocorreu um problema no cpf do usuario que vai pagar");
        }

        if (cobranca.getStatus() != StatusCobranca.PENDENTE) {
            throw new CobrancaExceptional("Essa cobrança já foi paga ou cancelada.");
        }

        Conta contaPagadora = cobranca.getDestinatario();
        Conta contaRecebedora = cobranca.getOriginador();

        if (contaPagadora.getSaldo().compareTo(cobranca.getValor()) < 0) {
            throw new CobrancaExceptional("Saldo insuficiente.");
        }

        Transacao transacao = new Transacao(contaPagadora, contaRecebedora, cobranca.getValor(), MeioDePagamento.SALDO,
                TipoDeTransacao.PAGAMENTO, StatusTransacao.SUCESSO);

        transacaoService.salvarTransacao(transacao);

        contaService.atualizarValorDaConta(contaPagadora, cobranca.getValor(), false);
        contaService.atualizarValorDaConta(contaRecebedora, cobranca.getValor(), true);

        cobranca.setStatus(StatusCobranca.PAGA);
        cobranca.setDataPagamento(LocalDateTime.now());
        cobranca.setMeioDePagamento(MeioDePagamento.SALDO);

        cobrancaService.salvarCobranca(cobranca);

    }

    @Transactional(rollbackFor = Exception.class)
    public void realizarPagamentoComCartao(Long idCobranca, PagamentoCartaoForm form)
            throws CobrancaExceptional, AutorizadorExternoException
    {
        Cobranca cobranca = cobrancaService.buscarCobrancaPorId(idCobranca);

        if (cobranca.getStatus() != StatusCobranca.PENDENTE) {
            throw new CobrancaExceptional("Essa cobrança já foi paga ou cancelada.");
        }

        cartaoService.validarDadosCartao(form);

        boolean autorizacaoExterna = false;
        try {
            autorizacaoExterna = autorizadorExternoService.isAutorizadoExternamente();
        } catch (AutorizadorExternoException e) {

            transacaoService.registrarTransacaoFalhaExterna(cobranca,MeioDePagamento.CREDITO);
            throw e;
        }

        Conta contaPagadora = cobranca.getDestinatario();
        Conta contaRecebedora = cobranca.getOriginador();


        if (!autorizacaoExterna) {
            transacaoService.registrarTransacaoFalhaExterna(cobranca,MeioDePagamento.CREDITO);
            throw new AutorizadorExternoException("Pagamento com cartão não autorizado pela autorizadora.");
        }

        contaService.atualizarValorDaConta(contaRecebedora, cobranca.getValor(), true);

        transacaoService.salvarTransacao(new Transacao(
                contaPagadora,
                contaRecebedora,
                cobranca.getValor(),
                MeioDePagamento.CREDITO,
                TipoDeTransacao.PAGAMENTO,
                StatusTransacao.SUCESSO
        ));

        cobranca.setStatus(StatusCobranca.PAGA);
        cobranca.setMeioDePagamento(MeioDePagamento.CREDITO);
        cobranca.setDataPagamento(LocalDateTime.now());
        cobrancaService.salvarCobranca(cobranca);
    }


}
