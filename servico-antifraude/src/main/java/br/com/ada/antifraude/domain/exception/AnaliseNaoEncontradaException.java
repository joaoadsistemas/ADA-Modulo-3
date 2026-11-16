package br.com.ada.antifraude.domain.exception;

import java.util.UUID;

public class AnaliseNaoEncontradaException extends RuntimeException {

    public AnaliseNaoEncontradaException(UUID transacaoId) {
        super("Análise não encontrada para a transação " + transacaoId);
    }
}
