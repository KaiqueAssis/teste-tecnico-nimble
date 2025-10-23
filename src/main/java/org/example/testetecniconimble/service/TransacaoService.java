package org.example.testetecniconimble.service;

import org.example.testetecniconimble.entity.Cobranca;
import org.example.testetecniconimble.entity.Transacao;
import org.example.testetecniconimble.entity.enums.MeioDePagamento;
import org.example.testetecniconimble.entity.enums.StatusTransacao;
import org.example.testetecniconimble.entity.enums.TipoDeTransacao;
import org.example.testetecniconimble.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;

    public TransacaoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    public void salvarTransacao(Transacao transacao){
        transacaoRepository.save(transacao);
    }

    public void registrarTransacaoFalhaExterna(Cobranca cobranca, MeioDePagamento meioDePagamento) {
        salvarTransacao(new Transacao(
                null,
                cobranca.getOriginador(),
                cobranca.getValor(),
                meioDePagamento,
                TipoDeTransacao.PAGAMENTO,
                StatusTransacao.FALHA
        ));
    }
}
