package br.com.ada.transacoes.domain.exception;

import java.util.UUID;

public class TransacaoNaoEncontradaException extends RuntimeException {

    public TransacaoNaoEncontradaException(UUID id) {
        super("Transação não encontrada: " + id);
    }
}
