package br.com.ada.antifraude.domain.service;

import br.com.ada.antifraude.domain.model.Resultado;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class RegrasAntifraudeTest {

    private final RegrasAntifraude regras = new RegrasAntifraude(new BigDecimal("5000.00"), Duration.ofMinutes(5), 3);

    @Test
    void aprovaTransacaoNormal() {
        assertThat(regras.avaliar(new BigDecimal("100.00"), 0).resultado()).isEqualTo(Resultado.APROVADA);
    }

    @Test
    void reprovaValorAcimaDoLimite() {
        assertThat(regras.avaliar(new BigDecimal("5000.01"), 0).resultado()).isEqualTo(Resultado.REPROVADA);
    }

    @Test
    void reprovaMuitasTransacoesNaJanela() {
        assertThat(regras.avaliar(new BigDecimal("10.00"), 3).resultado()).isEqualTo(Resultado.REPROVADA);
    }
}
