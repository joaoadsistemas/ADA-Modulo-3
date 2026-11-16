package br.com.ada.transacoes.api.controller;

import br.com.ada.transacoes.api.dto.TransacaoRequest;
import br.com.ada.transacoes.api.dto.TransacaoResponse;
import br.com.ada.transacoes.application.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransacaoResponse criar(@RequestBody @Valid TransacaoRequest request) {
        return TransacaoResponse.de(service.criar(request.contaOrigem(), request.contaDestino(), request.valor()));
    }

    @GetMapping("/{id}")
    public TransacaoResponse buscar(@PathVariable UUID id) {
        return TransacaoResponse.de(service.buscar(id));
    }

    @PostMapping("/{id}/reenviar")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TransacaoResponse reenviar(@PathVariable UUID id) {
        return TransacaoResponse.de(service.reenviar(id));
    }
}
