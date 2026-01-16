package com.exemplo.conta.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import com.exemplo.conta.domain.usuario.Usuario;

/**
 * Serviço responsável pela geração e validação de tokens JWT
 * 
 * Esta classe centraliza toda a lógica relacionada aos tokens JWT:
 * - Geração de tokens após autenticação bem-sucedida
 * - Validação de tokens em requisições protegidas
 * - Configuração de expiração e claims do token
 * 
 * Utiliza a biblioteca Auth0 JWT para operações criptográficas.
 * 
 * @author Guilherme - Dataprev
 */
@Service
public class TokenService {

    /**
     * Chave secreta para assinar tokens JWT
     * Obtida do application.properties para maior segurança
     * IMPORTANTE: Em produção, use uma chave complexa e armazene em variável de ambiente
     */
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um token JWT para o usuário autenticado
     * 
     * O token contém:
     * - Issuer: Identificação da aplicação que emitiu o token
     * - Subject: Login do usuário (identificador único)
     * - ExpiresAt: Data/hora de expiração (2 horas a partir da criação)
     * - Assinatura: Hash criptográfico para validação de integridade
     * 
     * @param usuario - Usuário para o qual será gerado o token
     * @return String - Token JWT assinado e codificado
     * @throws RuntimeException - Se houver erro na geração do token
     */
    public String gerarToken(Usuario usuario) {
        try {
            // Define o algoritmo de criptografia HMAC256 com a chave secreta
            var algoritmo = Algorithm.HMAC256(secret);
            
            // Cria e configura o token JWT
            return JWT.create()
                .withIssuer("API Conta Bancária")        // Emissor do token
                .withSubject(usuario.getLogin())         // Usuário (subject do token)
                .withExpiresAt(dataExpiracao())          // Data de expiração
                .withClaim("userId", usuario.getId())    // Claim adicional com ID do usuário
                .sign(algoritmo);                        // Assina o token com o algoritmo
                
        } catch (JWTCreationException exception) {
            // Log do erro para debug
            System.err.println("❌ Erro ao gerar token JWT: " + exception.getMessage());
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    /**
     * Valida um token JWT e retorna o subject (login do usuário)
     * 
     * Verifica:
     * - Assinatura do token (integridade)
     * - Emissor do token (issuer)
     * - Data de expiração
     * 
     * @param tokenJWT - Token a ser validado
     * @return String - Login do usuário se token válido
     * @throws RuntimeException - Se token inválido ou expirado
     */
    public String getSubject(String tokenJWT) {
        try {
            // Define o mesmo algoritmo usado na geração
            var algoritmo = Algorithm.HMAC256(secret);
            
            // Valida e decodifica o token
            return JWT.require(algoritmo)
                .withIssuer("API Conta Bancária")        // Verifica o emissor
                .build()
                .verify(tokenJWT)                        // Valida assinatura e expiração
                .getSubject();                           // Retorna o subject (login)
                
        } catch (JWTVerificationException exception) {
            // Log do erro para debug
            System.err.println("❌ Token JWT inválido: " + exception.getMessage());
            throw new RuntimeException("Token JWT inválido ou expirado");
        }
    }

    /**
     * Extrai o ID do usuário do token JWT
     * 
     * @param tokenJWT - Token JWT válido
     * @return Long - ID do usuário
     */
    public Long getUserId(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            
            return JWT.require(algoritmo)
                .withIssuer("API Conta Bancária")
                .build()
                .verify(tokenJWT)
                .getClaim("userId")
                .asLong();
                
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Erro ao extrair ID do usuário do token");
        }
    }

    /**
     * Calcula a data de expiração do token
     * 
     * Define o token para expirar em 2 horas a partir do momento atual.
     * Utiliza o fuso horário do Brasil (UTC-3).
     * 
     * @return Instant - Data/hora de expiração
     */
    private Instant dataExpiracao() {
        return LocalDateTime.now()
            .plusHours(2)                               // Adiciona 2 horas ao momento atual
            .toInstant(ZoneOffset.of("-03:00"));        // Converte para Instant com fuso horário do Brasil
    }
    
    /**
     * Verifica se um token está expirado (método auxiliar)
     * 
     * @param tokenJWT - Token a ser verificado
     * @return boolean - true se expirado, false caso contrário
     */
    public boolean isTokenExpired(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            var decodedJWT = JWT.require(algoritmo)
                .withIssuer("API Conta Bancária")
                .build()
                .verify(tokenJWT);
                
            return decodedJWT.getExpiresAt().before(java.util.Date.from(Instant.now()));
        } catch (Exception e) {
            return true; // Se não conseguir decodificar, considera expirado
        }
    }
}