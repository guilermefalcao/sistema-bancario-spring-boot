package com.exemplo.conta.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço de autenticação que implementa UserDetailsService
 * 
 * Esta classe é responsável por carregar os dados do usuário durante
 * o processo de autenticação do Spring Security. É chamada automaticamente
 * quando um usuário tenta fazer login no sistema.
 * 
 * @author Guilherme - Dataprev
 */
@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Carrega usuário pelo nome de usuário (login)
     * 
     * Este método é chamado automaticamente pelo Spring Security durante
     * o processo de autenticação. Busca o usuário no banco de dados
     * pelo login informado.
     * 
     * @param username - Login do usuário (nome de usuário)
     * @return UserDetails - Dados do usuário para autenticação
     * @throws UsernameNotFoundException - Se o usuário não for encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // Log para debug (pode ser removido em produção)
        System.out.println("🔍 Buscando usuário: " + username);
        
        // Busca o usuário no banco de dados pelo login
        UserDetails usuario = usuarioRepository.findByLogin(username);
        
        // Verifica se o usuário foi encontrado
        if (usuario == null) {
            System.out.println("❌ Usuário não encontrado: " + username);
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        
        // Log de sucesso (pode ser removido em produção)
        System.out.println("✅ Usuário encontrado: " + usuario.getUsername());
        
        return usuario;
    }
    
    /**
     * Método auxiliar para verificar se um usuário existe
     * 
     * @param login - Login a ser verificado
     * @return boolean - true se existe, false caso contrário
     */
    public boolean usuarioExiste(String login) {
        return usuarioRepository.existsByLogin(login);
    }
}