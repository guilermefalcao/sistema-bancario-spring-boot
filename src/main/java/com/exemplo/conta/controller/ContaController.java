package com.exemplo.conta.controller;

import com.exemplo.conta.entity.Conta;
import com.exemplo.conta.entity.Movimentacao;
import com.exemplo.conta.service.ContaService;
import com.exemplo.conta.dto.ContaPatchDTO;
import com.exemplo.conta.dto.ContaCompletaDTO;
import com.exemplo.conta.dto.MovimentacaoDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

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
@Tag(name = "2. Contas Bancárias", description = "Gerenciamento completo de contas bancárias (CRUD)")
@SecurityRequirement(name = "bearer-jwt")
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
    @Operation(
        summary = "📋 Listar todas as contas",
        description = "Retorna uma lista com todas as contas bancárias cadastradas no sistema, incluindo dados do cliente e saldo atual."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "✅ Lista de contas retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "🔒 Não autenticado - Token JWT inválido ou ausente")
    })
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
    @Operation(
        summary = "🔍 Buscar conta por ID",
        description = "Retorna os dados completos de uma conta específica pelo seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "✅ Conta encontrada"),
        @ApiResponse(responseCode = "404", description = "❌ Conta não encontrada"),
        @ApiResponse(responseCode = "401", description = "🔒 Não autenticado")
    })
    public ResponseEntity<Conta> buscarPorId(
        @Parameter(description = "ID da conta", example = "1") 
        @PathVariable Long id
    ) {
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
    @Operation(
        summary = "➕ Criar nova conta",
        description = "Cria uma nova conta bancária com cliente associado. O CPF deve ser único no sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "✅ Conta criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "❌ Dados inválidos ou CPF duplicado"),
        @ApiResponse(responseCode = "401", description = "🔒 Não autenticado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Dados da nova conta (nome, CPF e saldo inicial)",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                    {
                      "nome": "Maria Silva",
                      "cpf": "98765432100",
                      "saldo": 1000.00
                    }
                    """
            )
        )
    )
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
    @Operation(
        summary = "🗑️ Excluir conta",
        description = "Remove uma conta bancária do sistema. Esta operação é irreversível."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "✅ Conta excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "❌ Conta não encontrada"),
        @ApiResponse(responseCode = "401", description = "🔒 Não autenticado")
    })
    public ResponseEntity<Void> excluir(
        @Parameter(description = "ID da conta a ser excluída", example = "1")
        @PathVariable Long id
    ) {
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
    @Operation(
        summary = "✏️ Atualizar conta (completo)",
        description = "Atualiza todos os dados de uma conta existente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "✅ Conta atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "❌ Conta não encontrada"),
        @ApiResponse(responseCode = "401", description = "🔒 Não autenticado")
    })
    public ResponseEntity<Conta> atualizar(
        @Parameter(description = "ID da conta", example = "1")
        @PathVariable Long id, 
        @Valid @RequestBody Conta contaAtualizada
    ) {
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
    @Operation(
        summary = "🔧 Atualizar conta (parcial)",
        description = "Atualiza apenas os campos enviados, mantendo os demais inalterados."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "✅ Conta atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "❌ Conta não encontrada"),
        @ApiResponse(responseCode = "401", description = "🔒 Não autenticado")
    })
    public ResponseEntity<Conta> atualizarParcial(
        @Parameter(description = "ID da conta", example = "1")
        @PathVariable Long id, 
        @RequestBody ContaPatchDTO updates
    ) {
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
    @Operation(
        summary = "📋 Consultar extrato",
        description = "Retorna o histórico completo de movimentações (depósitos e saques) de uma conta."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "✅ Extrato retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "❌ Conta não encontrada"),
        @ApiResponse(responseCode = "401", description = "🔒 Não autenticado")
    })
    public List<Movimentacao> buscarExtrato(
        @Parameter(description = "ID da conta", example = "1")
        @PathVariable Long id
    ) {
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
    @Operation(
        summary = "💵 Realizar saque",
        description = "Realiza um saque na conta. O valor será deduzido do saldo e uma movimentação será registrada. Valida se há saldo suficiente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "✅ Saque realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "❌ Saldo insuficiente ou valor inválido"),
        @ApiResponse(responseCode = "404", description = "❌ Conta não encontrada"),
        @ApiResponse(responseCode = "401", description = "🔒 Não autenticado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Valor do saque",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                    {
                      "valor": 200.00
                    }
                    """
            )
        )
    )
    public ResponseEntity<Movimentacao> realizarSaque(
        @Parameter(description = "ID da conta", example = "1")
        @PathVariable Long id, 
        @Valid @RequestBody MovimentacaoDTO movimentacaoDTO
    ) {
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
    @Operation(
        summary = "💰 Realizar depósito",
        description = "Realiza um depósito na conta. O valor será adicionado ao saldo e uma movimentação será registrada."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "✅ Depósito realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "❌ Valor inválido"),
        @ApiResponse(responseCode = "404", description = "❌ Conta não encontrada"),
        @ApiResponse(responseCode = "401", description = "🔒 Não autenticado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Valor do depósito",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                    {
                      "valor": 500.00
                    }
                    """
            )
        )
    )
    public ResponseEntity<Movimentacao> realizarDeposito(
        @Parameter(description = "ID da conta", example = "1")
        @PathVariable Long id, 
        @Valid @RequestBody MovimentacaoDTO movimentacaoDTO
    ) {
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