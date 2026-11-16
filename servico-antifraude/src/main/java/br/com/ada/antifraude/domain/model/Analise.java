package br.com.ada.antifraude.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "analise", uniqueConstraints = @UniqueConstraint(name = "uk_analise_transacao_id", columnNames = "transacao_id"))
public class Analise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Chave de idempotência: o banco recusa uma segunda análise da mesma transação. */
    @Column(name = "transacao_id", nullable = false)
    private UUID transacaoId;

    @Column(nullable = false)
    private String contaOrigem;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Resultado resultado;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false)
    private Instant transacaoCriadaEm;

    @Column(nullable = false)
    private Instant analisadaEm;

    protected Analise() {
    }

    public Analise(UUID transacaoId, String contaOrigem, BigDecimal valor, Instant transacaoCriadaEm,
                   Resultado resultado, String motivo) {
        this.transacaoId = transacaoId;
        this.contaOrigem = contaOrigem;
        this.valor = valor;
        this.transacaoCriadaEm = transacaoCriadaEm;
        this.resultado = resultado;
        this.motivo = motivo;
        this.analisadaEm = Instant.now();
    }

    public void revisar(Resultado novoResultado, String novoMotivo) {
        this.resultado = novoResultado;
        this.motivo = novoMotivo;
        this.analisadaEm = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public UUID getTransacaoId() {
        return transacaoId;
    }

    public String getContaOrigem() {
        return contaOrigem;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public String getMotivo() {
        return motivo;
    }

    public Instant getTransacaoCriadaEm() {
        return transacaoCriadaEm;
    }

    public Instant getAnalisadaEm() {
        return analisadaEm;
    }
}
