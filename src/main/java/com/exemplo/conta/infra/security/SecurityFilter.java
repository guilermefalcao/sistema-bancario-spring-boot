package com.exemplo.conta.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.exemplo.conta.domain.usuario.UsuarioRepository;

/**
 * Filtro de segurança que intercepta todas as requisições HTTP
 * 
 * Este filtro é executado uma vez por requisição e tem a responsabilidade de:
 * - Extrair o token JWT do cabeçalho Authorization
 * - Validar o token usando o TokenService
 * - Autenticar o usuário no contexto de segurança do Spring
 * - Permitir ou bloquear o acesso baseado na validade do token
 * 
 * Estende OncePerRequestFilter para garantir execução única por requisição.
 * 
 * @author Guilherme - Dataprev
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Método principal do filtro - executado uma vez por requisição
     * 
     * Fluxo de execução:
     * 1. Extrai token JWT do cabeçalho Authorization
     * 2. Se token presente, valida e autentica usuário
     * 3. Se token ausente/inválido, requisição continua sem autenticação
     * 4. Spring Security decide se permite acesso baseado nas regras configuradas
     * 
     * @param request - Requisição HTTP recebida
     * @param response - Resposta HTTP a ser enviada
     * @param filterChain - Cadeia de filtros para continuar processamento
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException {

        // 1. EXTRAÇÃO DO TOKEN
        var tokenJWT = recuperarToken(request);
        
        // Log para debug (pode ser removido em produção)
        String uri = request.getRequestURI();
        String method = request.getMethod();
        System.out.println("🔍 " + method + " " + uri + " - Token: " + (tokenJWT != null ? "Presente" : "Ausente"));

        // 2. VALIDAÇÃO E AUTENTICAÇÃO
        if (tokenJWT != null) {
            try {
                // Valida o token e obtém o login do usuário
                var subject = tokenService.getSubject(tokenJWT);
                
                // Busca o usuário completo no banco de dados
                var usuario = usuarioRepository.findByLogin(subject);
                
                if (usuario != null) {
                    // Cria objeto de autenticação do Spring Security
                    var authentication = new UsernamePasswordAuthenticationToken(
                        usuario,                    // Principal (usuário autenticado)
                        null,                      // Credentials (não necessário após autenticação)
                        usuario.getAuthorities()   // Authorities (permissões do usuário)
                    );
                    
                    // Define o usuário como autenticado no contexto de segurança
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    System.out.println("✅ Usuário autenticado: " + usuario.getUsername());
                } else {
                    System.out.println("❌ Usuário não encontrado no banco: " + subject);
                }
                
            } catch (RuntimeException e) {
                // Token inválido ou expirado - usuário não será autenticado
                // Spring Security bloqueará automaticamente o acesso se necessário
                System.out.println("❌ Erro na validação do token: " + e.getMessage());
            }
        }

        // 3. CONTINUAÇÃO DA CADEIA DE FILTROS
        // Independente da autenticação, a requisição continua
        // Spring Security decidirá se permite acesso baseado nas regras configuradas
        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o token JWT do cabeçalho Authorization
     * 
     * Formato esperado: "Authorization: Bearer <token>"
     * Remove o prefixo "Bearer " e retorna apenas o token.
     * 
     * @param request - Requisição HTTP
     * @return String - Token JWT limpo (sem prefixo) ou null se não encontrado
     */
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        
        // Verifica se o cabeçalho existe e tem o formato correto
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Remove o prefixo "Bearer " (7 caracteres) e retorna o token
            return authorizationHeader.substring(7);
        }
        
        return null;
    }
    
    /**
     * Método auxiliar para verificar se a requisição precisa de autenticação
     * 
     * @param request - Requisição HTTP
     * @return boolean - true se é rota pública, false se precisa autenticação
     */
    private boolean isPublicRoute(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        
        // Rotas públicas que não precisam de token
        return (method.equals("POST") && uri.equals("/login")) ||
               uri.endsWith(".html") ||
               uri.endsWith(".css") ||
               uri.endsWith(".js") ||
               uri.equals("/");
    }
}