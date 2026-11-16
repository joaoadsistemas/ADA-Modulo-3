package br.com.ada.transacoes.application.service;

import br.com.ada.transacoes.domain.exception.TransacaoNaoEncontradaException;
import br.com.ada.transacoes.domain.model.Transacao;
import br.com.ada.transacoes.infrastructure.persistence.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransacaoService {

    private final TransacaoRepository repository;

    public TransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }

    /** Grava a transação no banco deste serviço. */
    public Transacao criar(String contaOrigem, String contaDestino, BigDecimal valor) {
        Transacao transacao = repository.save(new Transacao(contaOrigem, contaDestino, valor));
        return transacao;
    }

    public Transacao buscar(UUID id) {
        return repository.findById(id).orElseThrow(() -> new TransacaoNaoEncontradaException(id));
    }
}
