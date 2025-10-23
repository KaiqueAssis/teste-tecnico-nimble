package org.example.testetecniconimble.service;

import org.example.testetecniconimble.entity.*;
import org.example.testetecniconimble.entity.enums.MeioDePagamento;
import org.example.testetecniconimble.entity.enums.StatusCobranca;
import org.example.testetecniconimble.entity.enums.StatusTransacao;
import org.example.testetecniconimble.entity.enums.TipoDeTransacao;
import org.example.testetecniconimble.exception.AuthorizationException;
import org.example.testetecniconimble.exception.AutorizadorExternoException;
import org.example.testetecniconimble.exception.CancelamentoException;
import org.example.testetecniconimble.exception.CobrancaExceptional;
import org.springframework.stereotype.Service;

@Service
public class CancelamentoService {

    private final CobrancaService cobrancaService;
    private final UsuarioService usuarioService;
    private final ContaService contaService;
    private final TransacaoService transacaoService;
    private final CartaoService cartaoService;

    public CancelamentoService(CobrancaService cobrancaService,
                               UsuarioService usuarioService,
                               ContaService contaService,
                               TransacaoService transacaoService,
                               CartaoService cartaoService) {
        this.cobrancaService = cobrancaService;
        this.usuarioService = usuarioService;
        this.contaService = contaService;
        this.transacaoService = transacaoService;
        this.cartaoService = cartaoService;
    }

    public void cancelarCobrancaPorId(Long idCobranca, String cpfSolicitante) throws AutorizadorExternoException,
            CancelamentoException,
            AuthorizationException, CobrancaExceptional {
        Cobranca cobranca = cobrancaService.buscarCobrancaPorId(idCobranca);
        Usuario solicitante = usuarioService.buscarUsuarioPorCpf(cpfSolicitante);

        validarPermissaoDeCancelamento(cobranca, solicitante);

        switch (cobranca.getStatus()) {
            case PENDENTE -> cancelarCobrancaPendente(cobranca);
            case PAGA -> cancelarCobrancaPaga(cobranca);
            default -> throw new CancelamentoException("Cobrança já está cancelada");
        }

        cobrancaService.salvarCobranca(cobranca);
    }

    private void validarPermissaoDeCancelamento(Cobranca cobranca, Usuario solicitante) throws AuthorizationException {
        if (!cobranca.getOriginador().getUsuario().equals(solicitante)) {
            throw new AuthorizationException("Usuário não autorizado a cancelar esta cobrança.");
        }
    }

    private void cancelarCobrancaPendente(Cobranca cobranca) {
        cobranca.setStatus(StatusCobranca.CANCELADA);
    }

    private void cancelarCobrancaPaga(Cobranca cobranca) throws AutorizadorExternoException, CancelamentoException, CobrancaExceptional {
        MeioDePagamento meio = cobranca.getMeioDePagamento();

        if (meio == MeioDePagamento.SALDO) {
            processarEstornoSaldo(cobranca);
        } else if (meio == MeioDePagamento.CREDITO) {
            processarEstornoCredito(cobranca);
        } else {
            throw new CancelamentoException("Meio de pagamento inválido para cancelamento.");
        }

        cobranca.setStatus(StatusCobranca.CANCELADA);
    }

    private void processarEstornoSaldo(Cobranca cobranca) {
        Conta contaPagadora = cobranca.getOriginador();
        Conta contaRecebedora = cobranca.getDestinatario();

        Transacao estorno = new Transacao(
                contaPagadora,
                contaRecebedora,
                cobranca.getValor(),
                MeioDePagamento.SALDO,
                TipoDeTransacao.ESTORNO,
                StatusTransacao.SUCESSO
        );

        transacaoService.salvarTransacao(estorno);

        contaService.atualizarValorDaConta(contaPagadora, cobranca.getValor(), false);
        contaService.atualizarValorDaConta(contaRecebedora, cobranca.getValor(), true);
    }


    private void processarEstornoCredito(Cobranca cobranca) throws AutorizadorExternoException, CobrancaExceptional {
        cartaoService.cancelarPagamento(cobranca);

        Conta contaPagadora = cobranca.getOriginador();
        Conta contaRecebedora = cobranca.getDestinatario();

        Transacao estorno = new Transacao(
                contaPagadora,
                contaRecebedora,
                cobranca.getValor(),
                MeioDePagamento.CREDITO,
                TipoDeTransacao.ESTORNO,
                StatusTransacao.SUCESSO
        );

        transacaoService.salvarTransacao(estorno);
        contaService.atualizarValorDaConta(contaPagadora, cobranca.getValor(), false);
    }

}
