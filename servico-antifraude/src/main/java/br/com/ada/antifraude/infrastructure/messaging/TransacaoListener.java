package br.com.ada.antifraude.infrastructure.messaging;

import br.com.ada.antifraude.application.dto.TransacaoParaAnalise;
import br.com.ada.antifraude.application.service.AnaliseService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/** Adaptador de entrada: traduz a mensagem da fila para a camada de aplicação. */
@Component
public class TransacaoListener {

    private final AnaliseService analiseService;

    public TransacaoListener(AnaliseService analiseService) {
        this.analiseService = analiseService;
    }

    /** O ack só acontece depois que o método termina; se der erro, o RabbitMQ entrega de novo. */
    @RabbitListener(queues = "${fila.transacoes}")
    public void receber(TransacaoCriadaEvento evento) {
        analiseService.processar(new TransacaoParaAnalise(
                evento.transacaoId(), evento.contaOrigem(), evento.valor(), evento.criadaEm()));
    }
}
