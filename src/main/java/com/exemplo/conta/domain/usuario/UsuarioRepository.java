package com.exemplo.conta.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

/**
 * Repository para operações com a entidade Usuario
 * 
 * Interface que estende JpaRepository para operações CRUD automáticas
 * e define métodos customizados para busca de usuários na autenticação.
 * 
 * @author Guilherme - Dataprev
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca usuário por login (usado na autenticação)
     * 
     * Este método é chamado automaticamente pelo Spring Security
     * durante o processo de autenticação para carregar os dados do usuário.
     * 
     * @param login - Login do usuário a ser buscado
     * @return UserDetails - Dados do usuário para autenticação (ou null se não encontrado)
     */
    UserDetails findByLogin(String login);
    
    /**
     * Verifica se existe um usuário com o login informado
     * 
     * Método útil para validações antes de criar novos usuários
     * ou para verificar disponibilidade de login.
     * 
     * @param login - Login a ser verificado
     * @return boolean - true se existe, false caso contrário
     */
    boolean existsByLogin(String login);
}