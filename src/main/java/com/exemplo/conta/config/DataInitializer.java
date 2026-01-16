package com.exemplo.conta.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.exemplo.conta.domain.usuario.Usuario;
import com.exemplo.conta.domain.usuario.UsuarioRepository;

/**
 * Classe para inicializar dados básicos do sistema
 * 
 * Implementa CommandLineRunner para executar código após a aplicação iniciar.
 * Responsável por criar usuários padrão se não existirem no banco.
 * 
 * @author Guilherme - Dataprev
 */
@Component
@Order(1) // Executa primeiro
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Método executado automaticamente após a aplicação iniciar
     * Cria usuários padrão se não existirem
     */
    @Override
    public void run(String... args) throws Exception {
        
        System.out.println("🚀 Inicializando dados do sistema...");
        
        // Verifica se já existem usuários no banco
        long totalUsuarios = usuarioRepository.count();
        
        if (totalUsuarios == 0) {
            System.out.println("📝 Criando usuários padrão...");
            
            // Cria usuário admin
            criarUsuario("admin", "123456");
            
            // Cria usuário comum
            criarUsuario("usuario", "123456");
            
            // Cria usuário de teste
            criarUsuario("teste", "123456");
            
            System.out.println("✅ Usuários criados com sucesso!");
            
        } else {
            System.out.println("ℹ️ Usuários já existem no banco (" + totalUsuarios + " usuários)");
        }
        
        // Lista todos os usuários para verificação
        listarUsuarios();
    }
    
    /**
     * Cria um usuário no banco de dados
     * 
     * @param login - Login do usuário
     * @param senhaTexto - Senha em texto plano (será criptografada)
     */
    private void criarUsuario(String login, String senhaTexto) {
        try {
            // Verifica se o usuário já existe
            if (usuarioRepository.existsByLogin(login)) {
                System.out.println("⚠️ Usuário '" + login + "' já existe, pulando...");
                return;
            }
            
            // Criptografa a senha
            String senhaCriptografada = passwordEncoder.encode(senhaTexto);
            
            // Cria o usuário
            Usuario usuario = new Usuario(login, senhaCriptografada);
            
            // Salva no banco
            usuarioRepository.save(usuario);
            
            System.out.println("👤 Usuário criado: " + login + " (senha: " + senhaTexto + ")");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar usuário '" + login + "': " + e.getMessage());
        }
    }
    
    /**
     * Lista todos os usuários do sistema para verificação
     */
    private void listarUsuarios() {
        try {
            var usuarios = usuarioRepository.findAll();
            
            System.out.println("\n📋 Usuários no sistema:");
            System.out.println("========================");
            
            if (usuarios.isEmpty()) {
                System.out.println("❌ Nenhum usuário encontrado!");
            } else {
                for (Usuario usuario : usuarios) {
                    System.out.println("👤 ID: " + usuario.getId() + " | Login: " + usuario.getLogin());
                }
            }
            
            System.out.println("========================\n");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao listar usuários: " + e.getMessage());
        }
    }
}