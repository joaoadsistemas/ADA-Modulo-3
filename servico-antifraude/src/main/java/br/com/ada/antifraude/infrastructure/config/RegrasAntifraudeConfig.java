package br.com.ada.antifraude.infrastructure.config;

import br.com.ada.antifraude.domain.service.RegrasAntifraude;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Duration;

/** Monta as regras do domínio com os valores do application.yml. */
@Configuration
public class RegrasAntifraudeConfig {

    @Bean
    RegrasAntifraude regrasAntifraude(@Value("${antifraude.valor-maximo}") BigDecimal valorMaximo,
                                      @Value("${antifraude.janela-minutos}") long janelaMinutos,
                                      @Value("${antifraude.max-transacoes-na-janela}") long maxTransacoesNaJanela) {
        return new RegrasAntifraude(valorMaximo, Duration.ofMinutes(janelaMinutos), maxTransacoesNaJanela);
    }
}
