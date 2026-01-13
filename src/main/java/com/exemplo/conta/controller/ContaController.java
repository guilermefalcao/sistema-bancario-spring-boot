package com.exemplo.conta.controller;

import com.exemplo.conta.entity.Conta;
import com.exemplo.conta.entity.Movimentacao;
import com.exemplo.conta.service.ContaService;
import com.exemplo.conta.dto.ContaPatchDTO;
import com.exemplo.conta.dto.ContaCompletaDTO;
import com.exemplo.conta.dto.MovimentacaoDTO;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller REST para gerenciar Contas Bancárias
 * Endpoints disponíveis:
 * - GET /contas - Lista todas as contas
 * - GET /contas/{id} - Busca conta por ID
 * - POST /contas - Cria nova conta
 * - PUT /contas/{id} - Atualiza conta completa
 * - PATCH /contas/{id} - Atualiza conta parcial
 * - DELETE /contas/{id} - Exclui conta
 */
@RestController
@RequestMapping("/contas")
@CrossOrigin(origins = "*") // Permite CORS para todos os origins
public class ContaController {

    private final ContaService contaService;

    // Injeção de dependência via construtor (recomendado)
    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    /**
     * GET /contas
     * Lista todas as contas cadastradas
     * @return Lista de contas em formato JSON
     */
    @GetMapping
    public List<Conta> listar() {
        return contaService.listar();
    }

    /**
     * GET /contas/{id}
     * Busca uma conta específica pelo ID
     * @param id ID da conta a ser buscada
     * @return Conta encontrada ou erro 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Conta> buscarPorId(@PathVariable Long id) {
        Conta conta = contaService.buscarPorId(id);
        return ResponseEntity.ok(conta);
    }

    /**
     * POST /contas
     * Cria uma nova conta bancária com cliente
     * @param dto Dados completos (nome, cpf, saldo)
     * @return Conta criada com status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Conta> criar(@Valid @RequestBody ContaCompletaDTO dto) {
        Conta novaConta = contaService.criarContaCompleta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    /**
     * DELETE /contas/{id}
     * Exclui uma conta pelo ID
     * @param id ID da conta a ser excluída
     * @return Status 204 (No Content) se sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        contaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PUT /contas/{id}
     * Atualização completa da conta (todos os campos)
     * @param id ID da conta a ser atualizada
     * @param contaAtualizada Novos dados da conta
     * @return Conta atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<Conta> atualizar(@PathVariable Long id, @Valid @RequestBody Conta contaAtualizada) {
        Conta conta = contaService.atualizar(id, contaAtualizada);
        return ResponseEntity.ok(conta);
    }

    /**
     * PATCH /contas/{id}
     * Atualização parcial da conta (apenas campos enviados)
     * @param id ID da conta a ser atualizada
     * @param updates Campos a serem atualizados
     * @return Conta atualizada
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Conta> atualizarParcial(@PathVariable Long id, @RequestBody ContaPatchDTO updates) {
        Map<String, Object> mapUpdates = new HashMap<>();
        if (updates.getTitular() != null) mapUpdates.put("titular", updates.getTitular());
        if (updates.getSaldo() != null) mapUpdates.put("saldo", updates.getSaldo());
        
        Conta conta = contaService.atualizarParcial(id, mapUpdates);
        return ResponseEntity.ok(conta);
    }

    /**
     * GET /contas/{id}/extrato
     * Busca extrato de movimentações de uma conta
     * @param id ID da conta
     * @return Lista de movimentações
     */
    @GetMapping("/{id}/extrato")
    public List<Movimentacao> buscarExtrato(@PathVariable Long id) {
        return contaService.buscarExtrato(id);
    }

    /**
     * POST /contas/{id}/saque
     * Realiza saque em uma conta
     * @param id ID da conta
     * @param movimentacaoDTO Dados do saque
     * @return Movimentação criada
     */
    @PostMapping("/{id}/saque")
    public ResponseEntity<Movimentacao> realizarSaque(@PathVariable Long id, @Valid @RequestBody MovimentacaoDTO movimentacaoDTO) {
        Movimentacao movimentacao = contaService.realizarSaque(id, movimentacaoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacao);
    }

    /**
     * POST /contas/{id}/deposito
     * Realiza depósito em uma conta
     * @param id ID da conta
     * @param movimentacaoDTO Dados do depósito
     * @return Movimentação criada
     */
    @PostMapping("/{id}/deposito")
    public ResponseEntity<Movimentacao> realizarDeposito(@PathVariable Long id, @Valid @RequestBody MovimentacaoDTO movimentacaoDTO) {
        Movimentacao movimentacao = contaService.realizarDeposito(id, movimentacaoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacao);
    }
}




/*
 * O que mudou (conceito importante)
❌ Antes (errado)

Controller acessava ContaRepository diretamente

✅ Agora (correto)

Controller chama ContaService

Service concentra:

regras de negócio

validações

acesso ao banco

👉 Esse é o padrão usado em projetos reais e concursos
 */