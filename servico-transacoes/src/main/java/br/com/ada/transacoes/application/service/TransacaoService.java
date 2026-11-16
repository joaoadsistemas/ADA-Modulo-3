package br.com.ada.transacoes.application.service;

import br.com.ada.transacoes.application.port.TransacaoEventPublisher;
import br.com.ada.transacoes.domain.exception.TransacaoNaoEncontradaException;
import br.com.ada.transacoes.domain.model.Transacao;
import br.com.ada.transacoes.infrastructure.persistence.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransacaoService {

    private final TransacaoRepository repository;
    private final TransacaoEventPublisher publisher;

    public TransacaoService(TransacaoRepository repository, TransacaoEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    /** Grava a transação no banco deste serviço e manda para a fila de análise. */
    public Transacao criar(String contaOrigem, String contaDestino, BigDecimal valor) {
        Transacao transacao = repository.save(new Transacao(contaOrigem, contaDestino, valor));
        publisher.publicar(transacao);
        return transacao;
    }

    public Transacao buscar(UUID id) {
        return repository.findById(id).orElseThrow(() -> new TransacaoNaoEncontradaException(id));
    }

    /**
     * Manda de novo a MESMA mensagem para a fila. Serve para mostrar que,
     * se a mensagem chegar duas vezes, o antifraude grava uma vez só.
     */
    public Transacao reenviar(UUID id) {
        Transacao transacao = buscar(id);
        publisher.publicar(transacao);
        return transacao;
    }
}
