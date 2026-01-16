/**
 * =====================================================
 * GERENCIAMENTO DE AUTENTICAÇÃO JWT
 * =====================================================
 * 
 * Este arquivo contém toda a lógica de autenticação do frontend:
 * - Login e logout de usuários
 * - Armazenamento e validação de tokens JWT
 * - Interceptação de requisições para adicionar tokens
 * - Redirecionamento automático baseado no estado de autenticação
 * 
 * @author Guilherme - Dataprev
 */

// =====================================================
// CONFIGURAÇÕES GLOBAIS
// =====================================================

const API_BASE_URL = 'http://localhost:8080';
const TOKEN_KEY = 'jwt_token';
const USER_KEY = 'user_info';

// =====================================================
// CLASSE PRINCIPAL DE GERENCIAMENTO DE AUTENTICAÇÃO
// =====================================================

/**
 * Classe AuthManager - Gerencia toda a autenticação JWT
 * 
 * Funcionalidades:
 * - Realizar login e logout
 * - Verificar se usuário está autenticado
 * - Gerenciar tokens no localStorage
 * - Interceptar requisições HTTP
 * - Redirecionar usuários baseado no estado de auth
 */
class AuthManager {
    
    /**
     * Realiza login do usuário
     * 
     * Envia credenciais para o backend, recebe token JWT
     * e armazena no localStorage para uso futuro.
     * 
     * @param {Object} loginData - Dados de login {login, senha}
     * @returns {Object} - Resultado da operação {success, token?, message?}
     */
    static async login(loginData) {
        console.log('🔐 Iniciando processo de login para:', loginData.login);
        
        try {
            // Faz requisição para o endpoint de login
            const response = await fetch(`${API_BASE_URL}/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(loginData)
            });

            console.log('📡 Resposta do servidor:', response.status);

            if (response.ok) {
                // Login bem-sucedido
                const data = await response.json();
                
                // Armazena o token no localStorage
                localStorage.setItem(TOKEN_KEY, data.token);
                
                // Extrai e armazena informações do usuário do token
                const userInfo = this.extractUserInfo(data.token);
                localStorage.setItem(USER_KEY, JSON.stringify(userInfo));
                
                console.log('✅ Login realizado com sucesso');
                console.log('🎫 Token armazenado no localStorage');
                
                return { 
                    success: true, 
                    token: data.token,
                    user: userInfo
                };
                
            } else {
                // Erro na autenticação
                let errorMessage = 'Credenciais inválidas';
                
                try {
                    const errorData = await response.json();
                    errorMessage = errorData.message || errorMessage;
                } catch (e) {
                    // Se não conseguir parsear o JSON de erro, usa mensagem padrão
                }
                
                console.log('❌ Falha no login:', errorMessage);
                
                return { 
                    success: false, 
                    message: errorMessage 
                };
            }
            
        } catch (error) {
            console.error('💥 Erro de conexão no login:', error);
            
            return { 
                success: false, 
                message: 'Erro de conexão com o servidor. Verifique sua internet e tente novamente.' 
            };
        }
    }

    /**
     * Faz logout do usuário
     * 
     * Remove token e informações do usuário do localStorage
     * e redireciona para a página de login.
     */
    static logout() {
        console.log('🚪 Fazendo logout do usuário');
        
        // Remove dados do localStorage
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USER_KEY);
        
        console.log('🧹 Dados de autenticação removidos');
        
        // Redireciona para página de login
        window.location.href = '/login.html';
    }

    /**
     * Verifica se usuário está autenticado
     * 
     * Valida se existe token válido e não expirado no localStorage.
     * 
     * @returns {boolean} - true se autenticado, false caso contrário
     */
    static isAuthenticated() {
        const token = localStorage.getItem(TOKEN_KEY);
        
        if (!token) {
            console.log('🔓 Nenhum token encontrado');
            return false;
        }

        // Verifica se token não está expirado
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const now = Date.now() / 1000;
            
            if (payload.exp < now) {
                console.log('⏰ Token expirado, removendo...');
                // Token expirado - remove do localStorage
                localStorage.removeItem(TOKEN_KEY);
                localStorage.removeItem(USER_KEY);
                return false;
            }
            
            console.log('✅ Token válido encontrado');
            return true;
            
        } catch (error) {
            console.log('❌ Token inválido, removendo...');
            // Token inválido - remove do localStorage
            localStorage.removeItem(TOKEN_KEY);
            localStorage.removeItem(USER_KEY);
            return false;
        }
    }

    /**
     * Obtém o token JWT do localStorage
     * 
     * @returns {string|null} - Token JWT ou null se não existir
     */
    static getToken() {
        return localStorage.getItem(TOKEN_KEY);
    }

    /**
     * Obtém informações do usuário logado
     * 
     * @returns {Object|null} - Informações do usuário ou null
     */
    static getUserInfo() {
        const userInfo = localStorage.getItem(USER_KEY);
        return userInfo ? JSON.parse(userInfo) : null;
    }

    /**
     * Extrai informações do usuário do token JWT
     * 
     * @param {string} token - Token JWT
     * @returns {Object} - Informações do usuário
     */
    static extractUserInfo(token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return {
                login: payload.sub,
                userId: payload.userId,
                exp: payload.exp,
                iat: payload.iat
            };
        } catch (error) {
            console.error('Erro ao extrair informações do token:', error);
            return {};
        }
    }

    /**
     * Gera headers de autenticação para requisições
     * 
     * @returns {Object} - Headers com Authorization se token existir
     */
    static getAuthHeaders() {
        const token = this.getToken();
        
        const headers = {
            'Content-Type': 'application/json'
        };
        
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        
        return headers;
    }

    /**
     * Redireciona para login se não autenticado
     * 
     * @returns {boolean} - true se autenticado, false se redirecionou
     */
    static requireAuth() {
        if (!this.isAuthenticated()) {
            console.log('🔒 Acesso negado - redirecionando para login');
            window.location.href = '/login.html';
            return false;
        }
        return true;
    }

    /**
     * Intercepta fetch global para adicionar token automaticamente
     */
    static setupFetchInterceptor() {
        const originalFetch = window.fetch;
        
        window.fetch = function(url, options = {}) {
            // Adiciona headers de autenticação automaticamente
            options.headers = {
                ...options.headers,
                ...AuthManager.getAuthHeaders()
            };
            
            console.log('📡 Requisição interceptada:', url);
            
            return originalFetch(url, options)
                .then(response => {
                    // Se receber 401 (Unauthorized), token pode estar expirado
                    if (response.status === 401) {
                        console.log('🚫 Resposta 401 - Token expirado ou inválido');
                        AuthManager.logout();
                        return Promise.reject(new Error('Token expirado'));
                    }
                    return response;
                })
                .catch(error => {
                    console.error('💥 Erro na requisição:', error);
                    throw error;
                });
        };
        
        console.log('🔧 Interceptador de fetch configurado');
    }

    /**
     * Obtém tempo restante do token em minutos
     * 
     * @returns {number} - Minutos restantes ou 0 se expirado
     */
    static getTokenTimeRemaining() {
        const token = this.getToken();
        if (!token) return 0;
        
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const now = Date.now() / 1000;
            const remaining = payload.exp - now;
            return Math.max(0, Math.floor(remaining / 60));
        } catch (error) {
            return 0;
        }
    }
}

// =====================================================
// INICIALIZAÇÃO DA PÁGINA DE LOGIN
// =====================================================

/**
 * Configura eventos e comportamentos da página de login
 */
document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Inicializando página de login');
    
    // Se já está autenticado, redireciona para o sistema
    if (AuthManager.isAuthenticated()) {
        console.log('✅ Usuário já autenticado, redirecionando para sistema...');
        window.location.href = '/';
        return;
    }
    
    console.log('🔓 Usuário não autenticado, configurando formulário de login');
    
    // Configura o interceptador de fetch
    AuthManager.setupFetchInterceptor();
    
    // Obtém elementos do DOM
    const loginForm = document.getElementById('loginForm');
    const errorMessage = document.getElementById('errorMessage');
    const loginBtn = loginForm?.querySelector('.login-btn');
    const btnText = loginBtn?.querySelector('.btn-text');
    const btnLoading = loginBtn?.querySelector('.btn-loading');
    
    // Verifica se elementos existem
    if (!loginForm || !errorMessage || !loginBtn) {
        console.error('❌ Elementos do formulário não encontrados');
        return;
    }

    // Configura evento de submit do formulário
    loginForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        console.log('📝 Formulário de login submetido');

        // Obtém dados do formulário
        const formData = new FormData(loginForm);
        const loginData = {
            login: formData.get('login')?.trim(),
            senha: formData.get('senha')
        };

        // Validação básica no frontend
        if (!loginData.login || !loginData.senha) {
            showError('Por favor, preencha todos os campos.');
            return;
        }

        // Mostra estado de loading
        setLoading(true);
        hideMessage();

        try {
            // Realiza login
            const result = await AuthManager.login(loginData);

            if (result.success) {
                // Login bem-sucedido
                showSuccess('Login realizado com sucesso! Redirecionando...');
                
                // Redireciona após 1.5 segundos
                setTimeout(() => {
                    window.location.href = '/';
                }, 1500);
                
            } else {
                // Erro no login
                showError(result.message);
                setLoading(false);
            }
            
        } catch (error) {
            console.error('💥 Erro inesperado no login:', error);
            showError('Erro interno do sistema. Tente novamente.');
            setLoading(false);
        }
    });

    // =====================================================
    // FUNÇÕES AUXILIARES DA UI
    // =====================================================

    /**
     * Controla estado de loading do botão
     */
    function setLoading(loading) {
        if (!loginBtn || !btnText || !btnLoading) return;
        
        loginBtn.disabled = loading;
        btnText.style.display = loading ? 'none' : 'inline';
        btnLoading.style.display = loading ? 'inline' : 'none';
    }

    /**
     * Mostra mensagem de erro
     */
    function showError(message) {
        if (!errorMessage) return;
        
        errorMessage.textContent = message;
        errorMessage.className = 'error-message error';
        errorMessage.style.display = 'block';
    }

    /**
     * Mostra mensagem de sucesso
     */
    function showSuccess(message) {
        if (!errorMessage) return;
        
        errorMessage.textContent = message;
        errorMessage.className = 'error-message success';
        errorMessage.style.display = 'block';
    }

    /**
     * Esconde mensagens
     */
    function hideMessage() {
        if (!errorMessage) return;
        
        errorMessage.style.display = 'none';
    }
    
    // Auto-focus no campo de login
    const loginInput = document.getElementById('login');
    if (loginInput) {
        loginInput.focus();
    }
    
    console.log('✅ Página de login configurada com sucesso');
});

// =====================================================
// EXPORTAÇÃO GLOBAL
// =====================================================

// Torna AuthManager disponível globalmente
window.AuthManager = AuthManager;

// Log de carregamento do script
console.log('📜 Script auth.js carregado com sucesso');