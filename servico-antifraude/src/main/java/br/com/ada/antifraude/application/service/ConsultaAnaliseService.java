package br.com.ada.antifraude.application.service;

import br.com.ada.antifraude.application.dto.AnaliseResponse;
import br.com.ada.antifraude.domain.exception.AnaliseNaoEncontradaException;
import br.com.ada.antifraude.domain.model.Analise;
import br.com.ada.antifraude.domain.model.Resultado;
import br.com.ada.antifraude.infrastructure.persistence.AnaliseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Cache "analises" no Redis:
 * - o que guarda: o resultado da análise, pela chave transacaoId;
 * - por quanto tempo: 10 minutos (spring.cache.redis.time-to-live);
 * - quando apaga: ao vencer o TTL ou quando a análise é revisada manualmente.
 */
@Service
public class ConsultaAnaliseService {

    private static final Logger log = LoggerFactory.getLogger(ConsultaAnaliseService.class);

    private final AnaliseRepository repository;

    public ConsultaAnaliseService(AnaliseRepository repository) {
        this.repository = repository;
    }

    @Cacheable(cacheNames = "analises", key = "#transacaoId")
    public AnaliseResponse buscar(UUID transacaoId) {
        log.info("Cache miss: buscando no banco a análise da transação {}", transacaoId);
        return AnaliseResponse.de(encontrar(transacaoId));
    }

    @Transactional
    @CacheEvict(cacheNames = "analises", key = "#transacaoId")
    public AnaliseResponse revisar(UUID transacaoId, Resultado resultado, String motivo) {
        Analise analise = encontrar(transacaoId);
        analise.revisar(resultado, motivo);
        log.info("Análise revisada e removida do cache: transacaoId={}", transacaoId);
        return AnaliseResponse.de(analise);
    }

    private Analise encontrar(UUID transacaoId) {
        return repository.findByTransacaoId(transacaoId)
                .orElseThrow(() -> new AnaliseNaoEncontradaException(transacaoId));
    }
}
