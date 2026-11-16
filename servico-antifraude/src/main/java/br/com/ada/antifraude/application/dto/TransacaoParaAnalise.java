package br.com.ada.antifraude.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** Entrada da análise. A aplicação não conhece o formato da mensagem da fila. */
public record TransacaoParaAnalise(UUID transacaoId, String contaOrigem, BigDecimal valor, Instant criadaEm) {
}
