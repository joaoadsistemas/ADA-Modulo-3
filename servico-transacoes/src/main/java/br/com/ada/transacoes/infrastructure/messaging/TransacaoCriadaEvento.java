package br.com.ada.transacoes.infrastructure.messaging;

import br.com.ada.transacoes.domain.model.Transacao;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** Contrato da mensagem que vai pela fila. O transacaoId é a chave de idempotência de quem recebe. */
public record TransacaoCriadaEvento(UUID transacaoId, String contaOrigem, String contaDestino,
                                    BigDecimal valor, Instant criadaEm) {

    static TransacaoCriadaEvento de(Transacao t) {
        return new TransacaoCriadaEvento(t.getId(), t.getContaOrigem(), t.getContaDestino(), t.getValor(), t.getCriadaEm());
    }
}
