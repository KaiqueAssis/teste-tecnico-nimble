package org.example.testetecniconimble.service;

import org.example.testetecniconimble.dto.CobrancaDto;
import org.example.testetecniconimble.entity.*;
import org.example.testetecniconimble.entity.enums.StatusCobranca;
import org.example.testetecniconimble.exception.CobrancaExceptional;
import org.example.testetecniconimble.form.CobrancaForm;
import org.example.testetecniconimble.repository.CobrancaRepository;
import org.example.testetecniconimble.utils.ConversorMonetario;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;


@Service
public class CobrancaService {

    private final UsuarioService usuarioService;
    private final CobrancaRepository cobrancaRepository;
    private final ContaService contaService;

    public CobrancaService(UsuarioService usuarioService, CobrancaRepository cobrancaRepository,
                           ContaService contaService) {
        this.usuarioService = usuarioService;
        this.cobrancaRepository = cobrancaRepository;
        this.contaService = contaService;
    }

    public void criarCobranca(CobrancaForm form) throws CobrancaExceptional {

        if(form.valor().compareTo(BigDecimal.ZERO) < 0){
            throw new CobrancaExceptional("O Valor não pode ser negativo!");
        }
        Usuario usuarioOrigem = usuarioService.buscarUsuarioPorCpf(form.cpfOrigem());
        Usuario usuarioDestinatario = usuarioService.buscarUsuarioPorCpf(form.cpfDestinatario());
        BigInteger valorEmCentavos = ConversorMonetario.reaisParaCentavos(form.valor());

        Conta origemConta = contaService.buscarContaPorUsuario(usuarioOrigem);
        Conta destinatarioConta = contaService.buscarContaPorUsuario(usuarioDestinatario);

        cobrancaRepository.save(new Cobranca(origemConta, destinatarioConta, form.descricao(), valorEmCentavos));
    }

    public List<CobrancaDto> listarCobrancasCriadasPeloUsuario(String cpfOrigem, StatusCobranca statusCobranca){
        Usuario usuarioOrigem = usuarioService.buscarUsuarioPorCpf(cpfOrigem);

        return cobrancaRepository.listarCobrancaFeitasPeloOriginador(usuarioOrigem, statusCobranca)
                .stream()
                .map(CobrancaDto::new)
                .toList();
    }

    public List<CobrancaDto> listarCobrancasRecebidas(String cpfDestinatario, StatusCobranca statusCobranca){
        Usuario usuarioDestinatario = usuarioService.buscarUsuarioPorCpf(cpfDestinatario);

        return cobrancaRepository.listarCobrancaFeitasPeloOriginador(usuarioDestinatario, statusCobranca)
                .stream()
                .map(CobrancaDto::new)
                .toList();
    }

    public Cobranca buscarCobrancaPorId(Long id){
        return cobrancaRepository.findById(id);
    }

    public void salvarCobranca(Cobranca cobranca){
        cobrancaRepository.save(cobranca);
    }
}
