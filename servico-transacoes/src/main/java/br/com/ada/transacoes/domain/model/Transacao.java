package br.com.ada.transacoes.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transacao")
public class Transacao {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String contaOrigem;

    @Column(nullable = false)
    private String contaDestino;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private Instant criadaEm;

    protected Transacao() {
    }

    public Transacao(String contaOrigem, String contaDestino, BigDecimal valor) {
        this.id = UUID.randomUUID();
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.valor = valor;
        this.criadaEm = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getContaOrigem() {
        return contaOrigem;
    }

    public String getContaDestino() {
        return contaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Instant getCriadaEm() {
        return criadaEm;
    }
}
