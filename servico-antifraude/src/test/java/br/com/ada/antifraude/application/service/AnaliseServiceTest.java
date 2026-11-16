package br.com.ada.antifraude.application.service;

import br.com.ada.antifraude.application.dto.TransacaoParaAnalise;
import br.com.ada.antifraude.domain.model.Analise;
import br.com.ada.antifraude.domain.service.RegrasAntifraude;
import br.com.ada.antifraude.infrastructure.persistence.AnaliseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AnaliseServiceTest {

    private AnaliseRepository repository;
    private AnaliseService service;

    private final TransacaoParaAnalise transacao = new TransacaoParaAnalise(
            UUID.randomUUID(), "conta-1", new BigDecimal("100.00"), Instant.now());

    @BeforeEach
    void setUp() {
        repository = mock(AnaliseRepository.class);
        RegrasAntifraude regras = new RegrasAntifraude(new BigDecimal("5000.00"), Duration.ofMinutes(5), 3);
        service = new AnaliseService(repository, regras);
    }

    @Test
    void gravaAnaliseNaPrimeiraMensagem() {
        when(repository.existsByTransacaoId(transacao.transacaoId())).thenReturn(false);

        assertThat(service.processar(transacao)).isTrue();
        verify(repository).saveAndFlush(any(Analise.class));
    }

    @Test
    void ignoraMensagemRepetidaSemGravarDeNovo() {
        when(repository.existsByTransacaoId(transacao.transacaoId())).thenReturn(false, true);

        assertThat(service.processar(transacao)).isTrue();
        assertThat(service.processar(transacao)).isFalse();
        verify(repository, times(1)).saveAndFlush(any(Analise.class));
    }

    @Test
    void mensagemRepetidaAoMesmoTempoEBarradaPelaUniqueConstraint() {
        when(repository.existsByTransacaoId(transacao.transacaoId())).thenReturn(false);
        when(repository.saveAndFlush(any(Analise.class)))
                .thenThrow(new DataIntegrityViolationException("uk_analise_transacao_id"));

        assertThat(service.processar(transacao)).isFalse();
    }

    @Test
    void naoAvaliaNemGravaQuandoJaExiste() {
        when(repository.existsByTransacaoId(transacao.transacaoId())).thenReturn(true);

        assertThat(service.processar(transacao)).isFalse();
        verify(repository, never()).saveAndFlush(any(Analise.class));
    }
}
