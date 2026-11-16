package br.com.ada.antifraude.api.dto;

import br.com.ada.antifraude.domain.model.Resultado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RevisaoRequest(@NotNull Resultado resultado, @NotBlank String motivo) {
}
