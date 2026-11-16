package br.com.ada.antifraude.application.service;

import br.com.ada.antifraude.application.dto.AnaliseResponse;
import br.com.ada.antifraude.domain.exception.AnaliseNaoEncontradaException;
import br.com.ada.antifraude.domain.model.Analise;
import br.com.ada.antifraude.domain.model.Resultado;
import br.com.ada.antifraude.infrastructure.persistence.AnaliseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ConsultaAnaliseService {

    private static final Logger log = LoggerFactory.getLogger(ConsultaAnaliseService.class);

    private final AnaliseRepository repository;

    public ConsultaAnaliseService(AnaliseRepository repository) {
        this.repository = repository;
    }

    public AnaliseResponse buscar(UUID transacaoId) {
        log.info("Buscando no banco a análise da transação {}", transacaoId);
        return AnaliseResponse.de(encontrar(transacaoId));
    }

    @Transactional
    public AnaliseResponse revisar(UUID transacaoId, Resultado resultado, String motivo) {
        Analise analise = encontrar(transacaoId);
        analise.revisar(resultado, motivo);
        log.info("Análise revisada: transacaoId={}", transacaoId);
        return AnaliseResponse.de(analise);
    }

    private Analise encontrar(UUID transacaoId) {
        return repository.findByTransacaoId(transacaoId)
                .orElseThrow(() -> new AnaliseNaoEncontradaException(transacaoId));
    }
}
