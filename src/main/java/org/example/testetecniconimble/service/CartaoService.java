package org.example.testetecniconimble.service;

import org.example.testetecniconimble.entity.Cobranca;
import org.example.testetecniconimble.entity.enums.MeioDePagamento;
import org.example.testetecniconimble.entity.enums.StatusCobranca;
import org.example.testetecniconimble.exception.AutorizadorExternoException;
import org.example.testetecniconimble.exception.CobrancaExceptional;
import org.example.testetecniconimble.form.PagamentoCartaoForm;
import org.springframework.stereotype.Service;

@Service
public class CartaoService {

    private final AutorizadorExternoService autorizadorExternoService;

    public CartaoService(AutorizadorExternoService autorizadorExternoService) {
        this.autorizadorExternoService = autorizadorExternoService;
    }

    public void cancelarPagamento(Cobranca cobranca) throws AutorizadorExternoException, CobrancaExceptional {

        if (cobranca.getStatus() != StatusCobranca.PAGA || cobranca.getMeioDePagamento() != MeioDePagamento.CREDITO) {
            throw new CobrancaExceptional("Cobrança não elegível para cancelamento via cartão");
        }

        boolean autorizadorAprovou = autorizadorExternoService.solicitarEstorno(cobranca);

        if (!autorizadorAprovou) {
            throw new AutorizadorExternoException("Cancelamento do cartão não autorizado pelo provedor externo");
        }
    }


    public void validarDadosCartao(PagamentoCartaoForm form) throws CobrancaExceptional {
        if (form.numeroCartao() == null || form.numeroCartao().length() < 16) {
            throw new CobrancaExceptional("Número do cartão inválido.");
        }
        if (form.cvv() == null || form.cvv().length() != 3) {
            throw new CobrancaExceptional("CVV inválido.");
        }
        if (form.dataExpiracao() == null || !form.dataExpiracao().matches("\\d{2}/\\d{2}")) {
            throw new CobrancaExceptional("Data de expiração inválida (Formato MM/AA esperado).");
        }
    }
}
