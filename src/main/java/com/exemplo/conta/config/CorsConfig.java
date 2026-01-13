package com.exemplo.conta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração CORS (Cross-Origin Resource Sharing)
 * Permite que o frontend acesse a API REST
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configura CORS para permitir requisições do frontend
     * Permite todos os métodos HTTP (GET, POST, PUT, DELETE, PATCH)
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todos os endpoints
                .allowedOrigins("*") // Permite qualquer origem (para desenvolvimento)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*") // Permite todos os headers
                .allowCredentials(false); // Não permite credenciais para origem *
    }

    /**
     * Configuração alternativa usando CorsConfigurationSource
     * Mais flexível para configurações avançadas
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permite qualquer origem (para desenvolvimento)
        configuration.addAllowedOriginPattern("*");
        
        // Métodos HTTP permitidos
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("PATCH");
        configuration.addAllowedMethod("OPTIONS");
        
        // Headers permitidos
        configuration.addAllowedHeader("*");
        
        // Permite credenciais
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}