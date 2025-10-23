package org.example.testetecniconimble.service;

import org.example.testetecniconimble.entity.Conta;
import org.example.testetecniconimble.entity.Transacao;
import org.example.testetecniconimble.entity.enums.MeioDePagamento;
import org.example.testetecniconimble.entity.enums.StatusTransacao;
import org.example.testetecniconimble.entity.enums.TipoDeTransacao;
import org.example.testetecniconimble.exception.AutorizadorExternoException;
import org.example.testetecniconimble.exception.ContaNaoEncontradaExceptional;
import org.example.testetecniconimble.exception.DepositoException;
import org.example.testetecniconimble.form.DepositoForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;


@Service
public class DepositoService {

    private final ContaService contaService;
    private final AutorizadorExternoService autorizadorExternoService;
    private final TransacaoService transacaoService;

    public DepositoService(ContaService contaService, AutorizadorExternoService autorizadorExternoService,
                           TransacaoService transacaoService) {
        this.contaService = contaService;
        this.autorizadorExternoService = autorizadorExternoService;
        this.transacaoService = transacaoService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void realizarDeposito(DepositoForm form) throws DepositoException, ContaNaoEncontradaExceptional, AutorizadorExternoException {
        if (form.valor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DepositoException("O valor do depósito deve ser positivo.");
        }

        Conta contaDestino = contaService.buscarContaPorCpfDoUsuario(form.cpf());

        boolean autorizado = autorizadorExternoService.isAutorizadoExternamente();

        if (!autorizado) {
            throw new AutorizadorExternoException("Depósito negado pela autorizadora externa.");
        }

        BigInteger valorEmCentavos = form.valor()
                .multiply(BigDecimal.valueOf(100))
                .toBigIntegerExact();

        contaService.atualizarValorDaConta(contaDestino, valorEmCentavos, true);
        transacaoService.salvarTransacao(new Transacao(
                null,
                contaDestino,
                valorEmCentavos,
                MeioDePagamento.ESPECIE,
                TipoDeTransacao.DEPOSITO,
                StatusTransacao.SUCESSO
        ));
    }
}
