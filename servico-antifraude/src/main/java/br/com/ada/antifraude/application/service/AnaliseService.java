package br.com.ada.antifraude.application.service;

import br.com.ada.antifraude.application.dto.TransacaoParaAnalise;
import br.com.ada.antifraude.domain.model.Analise;
import br.com.ada.antifraude.domain.model.Veredito;
import br.com.ada.antifraude.domain.service.RegrasAntifraude;
import br.com.ada.antifraude.infrastructure.persistence.AnaliseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Processa as transações recebidas da fila de forma idempotente:
 * se a mesma transação chegar duas vezes, só a primeira é gravada.
 */
@Service
public class AnaliseService {

    private static final Logger log = LoggerFactory.getLogger(AnaliseService.class);

    private final AnaliseRepository repository;
    private final RegrasAntifraude regras;

    public AnaliseService(AnaliseRepository repository, RegrasAntifraude regras) {
        this.repository = repository;
        this.regras = regras;
    }

    /** @return true se gravou uma análise nova, false se a mensagem era repetida. */
    public boolean processar(TransacaoParaAnalise transacao) {
        // 1ª barreira: o caso comum de mensagem repetida (reentrega do RabbitMQ, reenvio do produtor).
        if (repository.existsByTransacaoId(transacao.transacaoId())) {
            log.info("Mensagem repetida ignorada: transacaoId={}", transacao.transacaoId());
            return false;
        }

        long recentes = repository.countByContaOrigemAndTransacaoCriadaEmAfter(
                transacao.contaOrigem(), transacao.criadaEm().minus(regras.janela()));
        Veredito veredito = regras.avaliar(transacao.valor(), recentes);
        Analise analise = new Analise(transacao.transacaoId(), transacao.contaOrigem(), transacao.valor(),
                transacao.criadaEm(), veredito.resultado(), veredito.motivo());

        try {
            repository.saveAndFlush(analise);
        } catch (DataIntegrityViolationException e) {
            // 2ª barreira: duas cópias processadas ao mesmo tempo. A unique constraint do banco decide.
            log.info("Mensagem repetida barrada pela unique constraint: transacaoId={}", transacao.transacaoId());
            return false;
        }
        log.info("Análise gravada: transacaoId={} resultado={}", transacao.transacaoId(), veredito.resultado());
        return true;
    }
}
