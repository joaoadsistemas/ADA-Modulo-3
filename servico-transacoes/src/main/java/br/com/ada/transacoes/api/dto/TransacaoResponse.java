package br.com.ada.transacoes.api.dto;

import br.com.ada.transacoes.domain.model.Transacao;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransacaoResponse(UUID id, String contaOrigem, String contaDestino, BigDecimal valor, Instant criadaEm) {

    public static TransacaoResponse de(Transacao t) {
        return new TransacaoResponse(t.getId(), t.getContaOrigem(), t.getContaDestino(), t.getValor(), t.getCriadaEm());
    }
}
