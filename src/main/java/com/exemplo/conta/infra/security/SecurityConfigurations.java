package com.exemplo.conta.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração principal do Spring Security
 * 
 * Esta classe define todas as regras de segurança da aplicação:
 * - Quais rotas são públicas e quais precisam de autenticação
 * - Configuração de sessões (stateless para JWT)
 * - Configuração de CORS e CSRF
 * - Integração com filtros customizados
 * - Beans necessários para autenticação
 * 
 * @Configuration - Indica que é uma classe de configuração do Spring
 * @EnableWebSecurity - Habilita a configuração de segurança web
 * 
 * @author Guilherme - Dataprev
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    /**
     * Configura a cadeia de filtros de segurança (Security Filter Chain)
     * 
     * Define as regras principais de segurança:
     * - Desabilita CSRF (não necessário para APIs stateless)
     * - Configura sessões como STATELESS (para JWT)
     * - Define rotas públicas e protegidas
     * - Adiciona filtro customizado para validação JWT
     * 
     * @param http - Objeto HttpSecurity para configuração
     * @return SecurityFilterChain - Cadeia de filtros configurada
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // ========================================
                // CONFIGURAÇÃO CSRF
                // ========================================
                // Desabilita proteção CSRF pois usaremos JWT (stateless)
                // CSRF é necessário apenas para aplicações com sessões
                .csrf(csrf -> csrf.disable())
                
                // ========================================
                // CONFIGURAÇÃO DE SESSÕES
                // ========================================
                // Configura sessão como STATELESS para JWT
                // Não mantém estado no servidor - tudo está no token
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // ========================================
                // CONFIGURAÇÃO DE AUTORIZAÇÃO
                // ========================================
                .authorizeHttpRequests(req -> {
                    // ROTAS PÚBLICAS (não precisam de autenticação)
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll();           // Endpoint de login
                    req.requestMatchers("/login.html").permitAll();                      // Página de login
                    req.requestMatchers("/login.css").permitAll();                       // CSS da página de login
                    req.requestMatchers("/auth.js").permitAll();                         // JavaScript de autenticação
                    req.requestMatchers("/").permitAll();                               // Página inicial (será protegida pelo JS)
                    req.requestMatchers("/index.html").permitAll();                     // Página principal (será protegida pelo JS)
                    
                    // ROTAS DO SWAGGER (documentação da API)
                    req.requestMatchers("/swagger-ui.html").permitAll();                // Página principal do Swagger
                    req.requestMatchers("/swagger-ui/**").permitAll();                  // Recursos do Swagger UI
                    req.requestMatchers("/v3/api-docs/**").permitAll();                 // JSON da documentação OpenAPI
                    req.requestMatchers("/swagger-resources/**").permitAll();           // Recursos adicionais
                    req.requestMatchers("/webjars/**").permitAll();                     // Bibliotecas JavaScript do Swagger
                    
                    // ROTAS PROTEGIDAS (precisam de autenticação JWT)
                    req.anyRequest().authenticated();                                    // Todas as outras rotas precisam de token
                })
                
                // ========================================
                // CONFIGURAÇÃO DE FILTROS
                // ========================================
                // Adiciona nosso filtro JWT ANTES do filtro padrão de autenticação
                // Isso garante que o token seja validado antes da verificação de credenciais
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                
                .build();
    }

    /**
     * Bean do AuthenticationManager para autenticação
     * 
     * O AuthenticationManager é responsável por processar tentativas de autenticação.
     * É usado no controller de login para validar credenciais do usuário.
     * 
     * @param configuration - Configuração de autenticação do Spring
     * @return AuthenticationManager - Gerenciador de autenticação configurado
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Bean do PasswordEncoder para criptografar senhas
     * 
     * BCryptPasswordEncoder é um algoritmo de hash seguro para senhas.
     * Características:
     * - Algoritmo adaptativo (fica mais lento com o tempo)
     * - Salt automático (previne ataques de rainbow table)
     * - Configuração de strength (complexidade do hash)
     * 
     * @return PasswordEncoder - Codificador de senhas BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usa BCrypt com strength padrão (10)
        // Strength maior = mais seguro, mas mais lento
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Método auxiliar para gerar hash BCrypt (usado em desenvolvimento)
     * 
     * Para gerar novos hashes de senha, descomente e execute:
     * System.out.println(passwordEncoder().encode("123456"));
     */
    // public void gerarHashSenha() {
    //     PasswordEncoder encoder = new BCryptPasswordEncoder();
    //     String senhaOriginal = "123456";
    //     String hashGerado = encoder.encode(senhaOriginal);
    //     System.out.println("Senha: " + senhaOriginal + " -> Hash: " + hashGerado);
    // }
}