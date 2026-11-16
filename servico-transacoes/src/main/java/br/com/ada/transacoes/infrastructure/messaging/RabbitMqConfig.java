package br.com.ada.transacoes.infrastructure.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    /** Fila durável: a mensagem sobrevive se o RabbitMQ reiniciar. Declarar de novo é inofensivo. */
    @Bean
    Queue filaTransacoes(@Value("${fila.transacoes}") String nome) {
        return new Queue(nome, true);
    }

    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
