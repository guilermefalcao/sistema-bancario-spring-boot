package com.exemplo.conta;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Teste rápido para verificar se a aplicação Spring Boot inicia
 * Execute esta classe para testar a inicialização
 */
public class TesteInicializacao {
    
    public static void main(String[] args) {
        System.out.println("=== INICIANDO TESTE DA APLICAÇÃO SPRING BOOT ===");
        
        try {
            // Inicia a aplicação Spring Boot
            ConfigurableApplicationContext context = SpringApplication.run(BackendContaApplication.class, args);
            
            System.out.println("✅ Aplicação iniciada com sucesso!");
            System.out.println("🌐 Servidor rodando em: http://localhost:8080");
            System.out.println("📋 Endpoints disponíveis:");
            System.out.println("   GET    /contas           - Listar todas as contas");
            System.out.println("   GET    /contas/{id}      - Buscar conta por ID");
            System.out.println("   POST   /contas           - Criar nova conta");
            System.out.println("   PUT    /contas/{id}      - Atualizar conta completa");
            System.out.println("   PATCH  /contas/{id}      - Atualizar conta parcial");
            System.out.println("   DELETE /contas/{id}      - Excluir conta");
            System.out.println("\n Pressione Ctrl+C para parar o servidor");
            
            // Mantém a aplicação rodando
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao iniciar a aplicação:");
            e.printStackTrace();
        }
    }
}