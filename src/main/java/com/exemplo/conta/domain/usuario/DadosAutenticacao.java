package com.exemplo.conta.domain.usuario;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO (Data Transfer Object) para receber dados de autenticação
 * 
 * Record que encapsula os dados necessários para login:
 * - login: nome de usuário
 * - senha: senha do usuário
 * 
 * Utiliza Bean Validation para garantir que os campos obrigatórios
 * sejam preenchidos antes do processamento da autenticação.
 * 
 * @author Guilherme - Dataprev
 */
public record DadosAutenticacao(
    
    /**
     * Login do usuário
     * Campo obrigatório - não pode ser nulo ou vazio
     */
    @NotBlank(message = "Login é obrigatório")
    String login,
    
    /**
     * Senha do usuário
     * Campo obrigatório - não pode ser nulo ou vazio
     */
    @NotBlank(message = "Senha é obrigatória")
    String senha
    
) {
    
    /**
     * Construtor adicional para facilitar criação em testes
     * 
     * @param login - Nome de usuário
     * @param senha - Senha do usuário
     */
    public static DadosAutenticacao of(String login, String senha) {
        return new DadosAutenticacao(login, senha);
    }
}