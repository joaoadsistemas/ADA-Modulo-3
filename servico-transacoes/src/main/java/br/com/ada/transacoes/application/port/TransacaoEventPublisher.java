package br.com.ada.transacoes.application.port;

import br.com.ada.transacoes.domain.model.Transacao;

/**
 * Porta de saída: a camada de aplicação só sabe que a transação "vai para a fila".
 * Quem implementa (RabbitMQ) fica em infrastructure.messaging.
 */
public interface TransacaoEventPublisher {

    void publicar(Transacao transacao);
}
