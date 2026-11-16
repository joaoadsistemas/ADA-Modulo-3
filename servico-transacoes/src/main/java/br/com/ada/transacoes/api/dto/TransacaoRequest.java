package br.com.ada.transacoes.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransacaoRequest(
        @NotBlank String contaOrigem,
        @NotBlank String contaDestino,
        @NotNull @DecimalMin("0.01") BigDecimal valor) {
}
