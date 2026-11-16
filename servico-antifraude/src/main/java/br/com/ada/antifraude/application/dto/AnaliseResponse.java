package br.com.ada.antifraude.application.dto;

import br.com.ada.antifraude.domain.model.Analise;
import br.com.ada.antifraude.domain.model.Resultado;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** É este objeto que fica no cache (Redis). */
public record AnaliseResponse(UUID transacaoId, String contaOrigem, BigDecimal valor,
                              Resultado resultado, String motivo, Instant analisadaEm) implements Serializable {

    public static AnaliseResponse de(Analise a) {
        return new AnaliseResponse(a.getTransacaoId(), a.getContaOrigem(), a.getValor(),
                a.getResultado(), a.getMotivo(), a.getAnalisadaEm());
    }
}
