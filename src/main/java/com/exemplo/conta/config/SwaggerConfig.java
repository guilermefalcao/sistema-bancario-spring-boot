package com.exemplo.conta.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do Swagger/OpenAPI 3.0
 * 
 * Esta classe configura a documentação automática da API REST usando Swagger.
 * O Swagger gera uma interface web interativa onde é possível:
 * - Visualizar todos os endpoints da API
 * - Testar requisições diretamente no navegador
 * - Ver exemplos de request/response
 * - Autenticar com JWT e testar rotas protegidas
 * 
 * Acesso à documentação:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 * 
 * @author Guilherme - Dataprev
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configura as informações gerais da API no Swagger
     * 
     * Define:
     * - Título e descrição do projeto
     * - Versão da API
     * - Informações de contato
     * - Configuração de segurança JWT
     * - Servidores disponíveis
     * 
     * @return OpenAPI - Objeto de configuração do Swagger
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // ========================================
                // INFORMAÇÕES GERAIS DA API
                // ========================================
                .info(new Info()
                        .title("🏦 API Sistema de Contas Bancárias")
                        .version("1.0.0")
                        .description("""
                                ## 📋 Sobre o Sistema
                                
                                Sistema bancário completo desenvolvido com **Spring Boot 3.3.8** + **Oracle Database** + **JWT Authentication**.
                                
                                ### ✨ Funcionalidades Principais
                                
                                - ✅ **Autenticação JWT**: Login seguro com tokens de 2 horas
                                - ✅ **Gestão de Contas**: CRUD completo de contas bancárias
                                - ✅ **Operações Bancárias**: Depósito e saque com validação
                                - ✅ **Extrato Bancário**: Histórico de todas as movimentações
                                - ✅ **Validações**: CPF único, saldo não negativo, dados obrigatórios
                                
                                ### 🔐 Como Usar a Autenticação
                                
                                1. **Faça login** no endpoint `POST /login` com credenciais válidas
                                2. **Copie o token JWT** retornado na resposta
                                3. **Clique no botão "Authorize" 🔓** no topo desta página
                                4. **Cole o token** no campo (sem prefixo "Bearer")
                                5. **Clique em "Authorize"** e depois "Close"
                                6. **Teste os endpoints protegidos** normalmente
                                
                                ### 👥 Usuários de Teste
                                
                                | Login | Senha | Descrição |
                                |-------|-------|-----------|
                                | admin | 123456 | Administrador |
                                | usuario | 123456 | Usuário comum |
                                | teste | 123456 | Usuário de testes |
                                
                                ### 🗄️ Tecnologias
                                
                                - **Spring Boot 3.3.8** - Framework principal
                                - **Spring Security** - Autenticação e autorização
                                - **JWT (Auth0)** - Tokens de autenticação
                                - **Spring Data JPA** - Persistência de dados
                                - **Oracle Database** - Banco de dados
                                - **Swagger/OpenAPI 3** - Documentação da API
                                
                                ### 📞 Suporte
                                
                                Em caso de dúvidas, consulte o README.md do projeto ou entre em contato com a equipe de desenvolvimento.
                                """)
                        .contact(new Contact()
                                .name("Guilherme - Dataprev")
                                .email("guilherme@dataprev.gov.br")
                                .url("https://github.com/seu-usuario/backend-conta"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                
                // ========================================
                // CONFIGURAÇÃO DE SEGURANÇA JWT
                // ========================================
                // Define que a API usa autenticação Bearer JWT
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("""
                                        ### 🔑 Autenticação JWT
                                        
                                        Insira o token JWT obtido no endpoint `/login`.
                                        
                                        **Não é necessário adicionar o prefixo "Bearer"** - ele será adicionado automaticamente.
                                        
                                        **Exemplo de token:**
                                        ```
                                        eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJBUEkgQ29udGEiLCJzdWIiOiJhZG1pbiIsImlkIjoxLCJleHAiOjE3MDUwNjg3NjB9.abc123...
                                        ```
                                        
                                        **Validade:** 2 horas após geração
                                        """)))
                
                // Aplica segurança JWT em todos os endpoints (exceto /login)
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
                
                // ========================================
                // SERVIDORES DISPONÍVEIS
                // ========================================
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor Local de Desenvolvimento"),
                        new Server()
                                .url("http://127.0.0.1:8080")
                                .description("Servidor Local (IP)")
                ));
    }
}
