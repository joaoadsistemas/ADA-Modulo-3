package br.com.ada.antifraude.api.controller;

import br.com.ada.antifraude.api.dto.RevisaoRequest;
import br.com.ada.antifraude.application.dto.AnaliseResponse;
import br.com.ada.antifraude.application.service.ConsultaAnaliseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/analises")
public class AnaliseController {

    private final ConsultaAnaliseService service;

    public AnaliseController(ConsultaAnaliseService service) {
        this.service = service;
    }

    @GetMapping("/{transacaoId}")
    public AnaliseResponse buscar(@PathVariable UUID transacaoId) {
        return service.buscar(transacaoId);
    }

    @PutMapping("/{transacaoId}/revisao")
    public AnaliseResponse revisar(@PathVariable UUID transacaoId, @RequestBody @Valid RevisaoRequest revisao) {
        return service.revisar(transacaoId, revisao.resultado(), revisao.motivo());
    }
}
