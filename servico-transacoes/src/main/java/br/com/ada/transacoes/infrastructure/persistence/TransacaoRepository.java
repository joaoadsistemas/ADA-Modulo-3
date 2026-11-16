package br.com.ada.transacoes.infrastructure.persistence;

import br.com.ada.transacoes.domain.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {
}
