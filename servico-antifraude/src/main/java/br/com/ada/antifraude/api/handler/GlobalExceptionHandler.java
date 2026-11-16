package br.com.ada.antifraude.api.handler;

import br.com.ada.antifraude.domain.exception.AnaliseNaoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Traduz as exceções do domínio para HTTP, para o domínio não depender da web. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AnaliseNaoEncontradaException.class)
    ProblemDetail naoEncontrada(AnaliseNaoEncontradaException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
