package br.com.ada.antifraude.domain.service;

import br.com.ada.antifraude.domain.model.Resultado;
import br.com.ada.antifraude.domain.model.Veredito;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * Regras de negócio puras (sem Spring e sem banco). São simples de propósito:
 * o foco do projeto é a fila e o cache, não o modelo de fraude.
 */
public class RegrasAntifraude {

    private final BigDecimal valorMaximo;
    private final Duration janela;
    private final long maxTransacoesNaJanela;

    public RegrasAntifraude(BigDecimal valorMaximo, Duration janela, long maxTransacoesNaJanela) {
        this.valorMaximo = valorMaximo;
        this.janela = janela;
        this.maxTransacoesNaJanela = maxTransacoesNaJanela;
    }

    /** Janela de tempo usada para contar as transações recentes da mesma conta. */
    public Duration janela() {
        return janela;
    }

    public Veredito avaliar(BigDecimal valor, long transacoesRecentesDaConta) {
        if (valor.compareTo(valorMaximo) > 0) {
            return new Veredito(Resultado.REPROVADA, "Valor acima do limite de " + valorMaximo);
        }
        if (transacoesRecentesDaConta >= maxTransacoesNaJanela) {
            return new Veredito(Resultado.REPROVADA,
                    "Mais de " + maxTransacoesNaJanela + " transações em " + janela.toMinutes() + " minutos");
        }
        return new Veredito(Resultado.APROVADA, "Nenhuma regra violada");
    }
}
