package com.exemplo.conta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import com.exemplo.conta.domain.usuario.DadosAutenticacao;
import com.exemplo.conta.domain.usuario.Usuario;
import com.exemplo.conta.infra.security.DadosTokenJWT;
import com.exemplo.conta.infra.security.TokenService;

/**
 * Controller responsável pela autenticação de usuários
 * 
 * Este controller expõe o endpoint de login da aplicação, onde os usuários
 * enviam suas credenciais (login e senha) e recebem um token JWT válido
 * em caso de autenticação bem-sucedida.
 * 
 * Fluxo de autenticação:
 * 1. Usuário envia POST /login com credenciais
 * 2. Spring Security valida as credenciais
 * 3. Se válidas, gera token JWT
 * 4. Retorna token para o cliente
 * 
 * @RestController - Indica que é um controller REST (retorna JSON)
 * @RequestMapping - Define o path base para todos os endpoints
 * 
 * @author Guilherme - Dataprev
 */
@RestController
@RequestMapping("/login")
@Tag(name = "1. Autenticação", description = "Endpoints para login e geração de token JWT")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    /**
     * Endpoint para autenticação de usuários
     * 
     * Recebe credenciais do usuário, valida através do Spring Security
     * e retorna um token JWT em caso de sucesso.
     * 
     * Método: POST
     * URL: /login
     * Content-Type: application/json
     * 
     * Body esperado:
     * {
     *   "login": "admin",
     *   "senha": "123456"
     * }
     * 
     * Resposta de sucesso (200 OK):
     * {
     *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     * }
     * 
     * Resposta de erro (401 Unauthorized):
     * Credenciais inválidas
     * 
     * @param dados - DTO com login e senha do usuário
     * @return ResponseEntity<DadosTokenJWT> - Token JWT se autenticação bem-sucedida
     */
    @PostMapping
    @Operation(
        summary = "🔐 Realizar Login",
        description = """
            Autentica um usuário e retorna um token JWT válido por 2 horas.
            
            **Usuários disponíveis para teste:**
            - Login: `admin` | Senha: `123456`
            - Login: `usuario` | Senha: `123456`
            - Login: `teste` | Senha: `123456`
            
            **Após receber o token:**
            1. Copie o valor do campo `token` da resposta
            2. Clique no botão **Authorize** 🔓 no topo da página
            3. Cole o token no campo (sem prefixo "Bearer")
            4. Clique em **Authorize** e depois **Close**
            5. Agora você pode testar os endpoints protegidos!
            """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "✅ Login realizado com sucesso - Token JWT gerado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DadosTokenJWT.class),
                examples = @ExampleObject(
                    name = "Token JWT",
                    value = """
                        {
                          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJBUEkgQ29udGEiLCJzdWIiOiJhZG1pbiIsImlkIjoxLCJleHAiOjE3MDUwNjg3NjB9.abc123xyz"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "❌ Dados inválidos - Campos obrigatórios não preenchidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "❌ Credenciais inválidas - Login ou senha incorretos",
            content = @Content(mediaType = "application/json")
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Credenciais do usuário (login e senha)",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = DadosAutenticacao.class),
            examples = {
                @ExampleObject(
                    name = "Admin",
                    value = """
                        {
                          "login": "admin",
                          "senha": "123456"
                        }
                        """
                ),
                @ExampleObject(
                    name = "Usuário",
                    value = """
                        {
                          "login": "usuario",
                          "senha": "123456"
                        }
                        """
                ),
                @ExampleObject(
                    name = "Teste",
                    value = """
                        {
                          "login": "teste",
                          "senha": "123456"
                        }
                        """
                )
            }
        )
    )
    @SecurityRequirement(name = "") // Remove autenticação deste endpoint
    public ResponseEntity<DadosTokenJWT> login(@RequestBody @Valid DadosAutenticacao dados) {
        
        // Log da tentativa de login (pode ser removido em produção)
        System.out.println("🔐 Tentativa de login para usuário: " + dados.login());
        
        try {
            // ========================================
            // ETAPA 1: CRIAÇÃO DO TOKEN DE AUTENTICAÇÃO
            // ========================================
            // Cria um token de autenticação com as credenciais recebidas
            // Este token será usado pelo AuthenticationManager para validar o usuário
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                dados.login(),    // Principal (nome de usuário)
                dados.senha()     // Credentials (senha)
            );

            // ========================================
            // ETAPA 2: AUTENTICAÇÃO VIA SPRING SECURITY
            // ========================================
            // O AuthenticationManager:
            // 1. Chama o AutenticacaoService.loadUserByUsername()
            // 2. Compara a senha fornecida com a hash do banco (BCrypt)
            // 3. Se válidas, retorna um Authentication com o usuário
            // 4. Se inválidas, lança BadCredentialsException
            var authentication = authenticationManager.authenticate(authenticationToken);

            // ========================================
            // ETAPA 3: OBTENÇÃO DO USUÁRIO AUTENTICADO
            // ========================================
            // Extrai o usuário do objeto Authentication
            // getPrincipal() retorna o UserDetails (nossa classe Usuario)
            var usuario = (Usuario) authentication.getPrincipal();
            
            System.out.println("✅ Autenticação bem-sucedida para: " + usuario.getUsername());

            // ========================================
            // ETAPA 4: GERAÇÃO DO TOKEN JWT
            // ========================================
            // Gera o token JWT usando o TokenService
            // O token contém informações do usuário e tempo de expiração
            var tokenJWT = tokenService.gerarToken(usuario);
            
            System.out.println("🎫 Token JWT gerado com sucesso");

            // ========================================
            // ETAPA 5: RETORNO DA RESPOSTA
            // ========================================
            // Retorna o token JWT no corpo da resposta com status 200 OK
            return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
            
        } catch (Exception e) {
            // Log do erro de autenticação
            System.out.println("❌ Falha na autenticação para usuário: " + dados.login());
            System.out.println("❌ Erro: " + e.getMessage());
            
            // O Spring Security automaticamente retorna 401 Unauthorized
            // para BadCredentialsException, então re-lançamos a exceção
            throw e;
        }
    }
    
    /**
     * Endpoint auxiliar para verificar status da API (opcional)
     * 
     * GET /login/status
     * Retorna informações sobre o serviço de autenticação
     */
    // @GetMapping("/status")
    // public ResponseEntity<Map<String, String>> status() {
    //     Map<String, String> status = new HashMap<>();
    //     status.put("service", "Authentication Service");
    //     status.put("status", "UP");
    //     status.put("timestamp", LocalDateTime.now().toString());
    //     return ResponseEntity.ok(status);
    // }
}