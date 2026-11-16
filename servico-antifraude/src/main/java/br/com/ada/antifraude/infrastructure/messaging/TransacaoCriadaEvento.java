package br.com.ada.antifraude.infrastructure.messaging;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** Cópia do contrato da mensagem enviada pelo servico-transacoes (os serviços não compartilham código). */
public record TransacaoCriadaEvento(UUID transacaoId, String contaOrigem, String contaDestino,
                                    BigDecimal valor, Instant criadaEm) {
}
