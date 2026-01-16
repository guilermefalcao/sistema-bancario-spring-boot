package com.exemplo.conta.infra.security;

/**
 * DTO (Data Transfer Object) para retorno do token JWT
 * 
 * Record simples que encapsula o token JWT gerado após
 * uma autenticação bem-sucedida. É retornado no endpoint
 * de login como resposta JSON.
 * 
 * Exemplo de uso:
 * {
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
 * }
 * 
 * @author Guilherme - Dataprev
 */
public record DadosTokenJWT(
    
    /**
     * Token JWT gerado para o usuário autenticado
     * Contém informações codificadas sobre o usuário e tempo de expiração
     */
    String token
    
) {
    
    /**
     * Método estático para criação mais legível
     * 
     * @param token - Token JWT a ser encapsulado
     * @return DadosTokenJWT - Instância do DTO
     */
    public static DadosTokenJWT of(String token) {
        return new DadosTokenJWT(token);
    }
}