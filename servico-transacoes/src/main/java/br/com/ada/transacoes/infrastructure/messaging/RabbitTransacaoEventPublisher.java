package br.com.ada.transacoes.infrastructure.messaging;

import br.com.ada.transacoes.application.port.TransacaoEventPublisher;
import br.com.ada.transacoes.domain.model.Transacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Adaptador da porta TransacaoEventPublisher para o RabbitMQ. */
@Component
public class RabbitTransacaoEventPublisher implements TransacaoEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitTransacaoEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String fila;

    public RabbitTransacaoEventPublisher(RabbitTemplate rabbitTemplate, @Value("${fila.transacoes}") String fila) {
        this.rabbitTemplate = rabbitTemplate;
        this.fila = fila;
    }

    @Override
    public void publicar(Transacao transacao) {
        TransacaoCriadaEvento evento = TransacaoCriadaEvento.de(transacao);
        // Exchange padrão (""): a routing key é o nome da fila.
        rabbitTemplate.convertAndSend("", fila, evento, mensagem -> {
            mensagem.getMessageProperties().setMessageId(evento.transacaoId().toString());
            return mensagem;
        });
        log.info("Mensagem enviada para a fila {}: transacaoId={}", fila, evento.transacaoId());
    }
}
