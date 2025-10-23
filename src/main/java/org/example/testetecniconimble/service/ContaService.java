package org.example.testetecniconimble.service;

import org.example.testetecniconimble.dto.ContaDto;
import org.example.testetecniconimble.entity.Conta;
import org.example.testetecniconimble.entity.Usuario;
import org.example.testetecniconimble.exception.ContaNaoEncontradaExceptional;
import org.example.testetecniconimble.repository.ContaRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Service
public class ContaService {

    private final ContaRepository repository;

    public ContaService(ContaRepository contaRepository) {
        this.repository = contaRepository;
    }

    public Conta buscarContaPorUsuario(Usuario usuario){
        return repository.findByUsuario(usuario).orElseThrow();
    }

    public ContaDto buscarContaPorCpfUser(String cpf) throws ContaNaoEncontradaExceptional {
        return buscarContaPorCpfDoUsuario(cpf).converterEmDto();
    }

    public void atualizarValorDaConta(Conta conta, BigInteger valor, boolean vaiSomar){
        if(vaiSomar){
            conta.setSaldo(conta.getSaldo().add(valor));
        } else {
            conta.setSaldo(conta.getSaldo().subtract(valor));
        }

        conta.setUltimaAtualizacao(LocalDateTime.now());
        repository.save(conta);
    }

    public Conta buscarContaPorCpfDoUsuario(String cpf) throws ContaNaoEncontradaExceptional {
        return repository.findByUsuario_Cpf(cpf)
                .orElseThrow(() -> new ContaNaoEncontradaExceptional("A conta não foi encontrada usando o cpf " + cpf));
    }

    public void criarConta(Usuario usuario){
        repository.save(new Conta(usuario));
    }
}
