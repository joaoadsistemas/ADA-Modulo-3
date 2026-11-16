package br.com.ada.antifraude.infrastructure.persistence;

import br.com.ada.antifraude.domain.model.Analise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface AnaliseRepository extends JpaRepository<Analise, Long> {

    boolean existsByTransacaoId(UUID transacaoId);

    Optional<Analise> findByTransacaoId(UUID transacaoId);

    long countByContaOrigemAndTransacaoCriadaEmAfter(String contaOrigem, Instant desde);
}
