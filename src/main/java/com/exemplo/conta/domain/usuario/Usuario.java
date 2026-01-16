package com.exemplo.conta.domain.usuario;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade Usuario que implementa UserDetails do Spring Security
 * 
 * Esta classe representa um usuário do sistema com credenciais de acesso.
 * Implementa UserDetails para integração com Spring Security e autenticação JWT.
 * 
 * @author Guilherme - Dataprev
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String login;    // Nome de usuário para login
    private String senha;    // Senha criptografada com BCrypt

    // ========================================
    // CONSTRUTORES
    // ========================================
    
    /**
     * Construtor padrão (necessário para JPA)
     */
    public Usuario() {}

    /**
     * Construtor com parâmetros para criação de usuário
     * @param login - Nome de usuário
     * @param senha - Senha (será criptografada)
     */
    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    // ========================================
    // GETTERS E SETTERS
    // ========================================
    
    public Long getId() { 
        return id; 
    }
    
    public String getLogin() { 
        return login; 
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }

    // ========================================
    // MÉTODOS OBRIGATÓRIOS DA INTERFACE UserDetails
    // ========================================
    
    /**
     * Retorna as permissões/roles do usuário
     * Por enquanto, todos os usuários têm a role "ROLE_USER"
     * Em futuras versões, pode ser expandido para diferentes níveis de acesso
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * Retorna a senha do usuário (criptografada)
     * Usado pelo Spring Security para validação
     */
    @Override
    public String getPassword() {
        return senha;
    }

    /**
     * Retorna o nome de usuário (login)
     * Usado pelo Spring Security como identificador único
     */
    @Override
    public String getUsername() {
        return login;
    }

    /**
     * Indica se a conta do usuário não está expirada
     * Retorna true pois nossa aplicação não expira contas
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica se a conta do usuário não está bloqueada
     * Retorna true pois nossa aplicação não bloqueia contas
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica se as credenciais (senha) não estão expiradas
     * Retorna true pois nossa aplicação não expira senhas
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica se o usuário está habilitado/ativo
     * Retorna true pois todos os usuários estão ativos por padrão
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}