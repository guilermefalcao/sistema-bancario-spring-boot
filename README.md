# 🏦 Sistema de Contas Bancárias - Spring Boot

Sistema bancário completo desenvolvido com **Spring Boot 3.3.8** + **Oracle Database** + **Frontend responsivo**.

## 🚀 Funcionalidades

- ✅ **Autenticação JWT**: Login seguro com tokens de 2 horas
- ✅ **Gestão de Contas**: Criar, listar, editar, excluir
- ✅ **Operações Bancárias**: Depósito, saque com validação de saldo
- ✅ **Extrato Bancário**: Histórico completo de movimentações
- ✅ **Busca Inteligente**: Por ID ou nome do cliente
- ✅ **Interface Web**: Frontend responsivo e moderno
- ✅ **API REST**: Endpoints completos para integração
- ✅ **Documentação Swagger**: Interface interativa para testar a API
- ✅ **Validações**: CPF único, saldo não negativo, dados obrigatórios

## 🛠️ Tecnologias

### Backend
- **Spring Boot 3.3.8** - Framework principal
- **Spring Security** - Autenticação e autorização
- **JWT (Auth0)** - Tokens de autenticação
- **Spring Data JPA** - Persistência de dados
- **Spring Web** - API REST
- **Spring Validation** - Validação de dados
- **Swagger/OpenAPI 3** - Documentação da API
- **Oracle Database** - Banco de dados
- **Maven** - Gerenciamento de dependências

### Frontend
- **HTML5** - Estrutura
- **CSS3** - Estilização responsiva
- **JavaScript ES6** - Interatividade
- **Fetch API** - Comunicação com backend

## 📊 Arquitetura

```
┌─────────────────┐
│   Frontend      │ ← HTML/CSS/JavaScript
├─────────────────┤
│   Controller    │ ← REST API Endpoints
├─────────────────┤
│   Service       │ ← Regras de Negócio
├─────────────────┤
│   Repository    │ ← Acesso aos Dados
├─────────────────┤
│   Entity        │ ← Mapeamento JPA
├─────────────────┤
│   Database      │ ← Oracle Database
└─────────────────┘
```

## 🗄️ Modelo de Dados

### Tabelas Oracle
- **CLIENTE** - Dados pessoais (ID, Nome, CPF, Data Cadastro)
- **CONTA** - Contas bancárias (ID, ID_Cliente, Saldo)
- **MOVIMENTACAO** - Histórico (ID, ID_Conta, Tipo, Valor, Data)

## 🔗 Endpoints API

### Autenticação
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/login` | Realizar login e obter token JWT |

### Contas Bancárias
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/contas` | Lista todas as contas |
| `GET` | `/contas/{id}` | Busca conta por ID |
| `POST` | `/contas` | Cria nova conta |
| `PUT` | `/contas/{id}` | Atualiza conta |
| `DELETE` | `/contas/{id}` | Exclui conta |
| `GET` | `/contas/{id}/extrato` | Consulta extrato |
| `POST` | `/contas/{id}/deposito` | Realiza depósito |
| `POST` | `/contas/{id}/saque` | Realiza saque |

### Documentação
| URL | Descrição |
|-----|------------|
| `/swagger-ui.html` | Interface Swagger para testar API |
| `/v3/api-docs` | Especificação OpenAPI (JSON) |

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.6+
- Oracle Database XE 21c

### 1. Configurar Banco Oracle
```sql
-- Executar scripts na ordem:
1. criar-tabela-oracle.sql
2. criar-sequencias.sql
3. povoar-tabelas-oracle.sql (opcional)
```

### 2. Configurar Conexão
Editar `application.properties`:
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
spring.datasource.username=CONTA_APP
spring.datasource.password=1234
```

### 3. Executar Aplicação
```bash
mvn clean compile
mvn spring-boot:run
```

### 4. Acessar Sistema
- **Frontend:** http://localhost:8080 ou http://127.0.0.1:8080
- **Login:** http://localhost:8080/login.html
- **API REST:** http://localhost:8080/contas
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

## 📚 Documentação Swagger

### O que é Swagger?
Swagger (OpenAPI) é uma ferramenta de **documentação interativa** que permite:
- 📝 Visualizar todos os endpoints da API
- 🧪 Testar requisições diretamente no navegador
- 🔐 Autenticar com JWT e testar rotas protegidas
- 📊 Ver exemplos de request/response
- 📚 Entender contratos da API

### Como Usar o Swagger

1. **Acesse o Swagger UI:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

2. **Faça Login:**
   - Clique em `POST /login`
   - Clique em "Try it out"
   - Use: `{"login": "admin", "senha": "123456"}`
   - Clique em "Execute"
   - Copie o token JWT da resposta

3. **Autorize:**
   - Clique no botão "🔓 Authorize" no topo
   - Cole o token (sem "Bearer")
   - Clique em "Authorize" e depois "Close"

4. **Teste os Endpoints:**
   - Navegue pelos endpoints
   - Clique em "Try it out"
   - Preencha os parâmetros
   - Clique em "Execute"

### Usuários de Teste
| Login | Senha | Descrição |
|-------|-------|------------|
| admin | 123456 | Administrador |
| usuario | 123456 | Usuário comum |
| teste | 123456 | Usuário de testes |

### Documentação Completa
Para um guia detalhado com exemplos e troubleshooting, abra:
```
GUIA_SWAGGER.html
```

## 📱 Interface Web

### Funcionalidades da Interface
- 🔍 **Busca de contas** por ID ou nome
- 📝 **Formulário de criação** com validação
- ✏️ **Edição inline** sem popups
- 📋 **Extrato expandível** com operações
- 💰 **Depósito/Saque** com validação de saldo
- 🗑️ **Exclusão** com confirmação

### Screenshots
```
🏦 ID: 1 | 👤 João Silva (CPF: 12345678901)
💰 R$ 1500.00
[📋 Extrato] [✏️ Editar] [🗑️ Excluir]

┌─────────────────────────────────────────────┐
│ 📋 Extrato da Conta                        │
│ [💰 Depósito] [💵 Saque] 💰 Saldo: R$ 1500 │
│                                             │
│ DEPOSITO    06/01/2026, 09:02:40  + R$ 500 │
│ SAQUE       06/01/2026, 08:58:33  - R$ 200 │
└─────────────────────────────────────────────┘
```

## 🧪 Testes

### Testar com Swagger (Recomendado)
1. Acesse: http://localhost:8080/swagger-ui.html
2. Faça login e obtenha o token JWT
3. Clique em "Authorize" e cole o token
4. Teste todos os endpoints interativamente

### Testar API com cURL
```bash
# Listar contas
curl -X GET http://localhost:8080/contas

# Criar conta
curl -X POST http://localhost:8080/contas \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","cpf":"12345678901","saldo":1000.00}'

# Realizar depósito
curl -X POST http://localhost:8080/contas/1/deposito \
  -H "Content-Type: application/json" \
  -d '{"valor":500.00}'
```

## 📚 Conceitos Demonstrados

### Spring Boot
- ✅ **Auto-configuração** e starter dependencies
- ✅ **Injeção de dependência** via construtor
- ✅ **Camadas arquiteturais** (Controller → Service → Repository)
- ✅ **Bean Validation** com anotações
- ✅ **Tratamento de exceções** global
- ✅ **Transações** automáticas
- ✅ **Segurança JWT** com Spring Security
- ✅ **Documentação Swagger** automática

### Boas Práticas
- ✅ **DTOs** para transferência de dados
- ✅ **Repository Pattern** para acesso aos dados
- ✅ **Service Layer** para regras de negócio
- ✅ **CORS** configurado para frontend
- ✅ **Validações** robustas de entrada
- ✅ **Código limpo** e bem documentado

## 📁 Estrutura do Projeto

```
backend-conta/
├── src/main/java/com/exemplo/conta/
│   ├── controller/          # REST Controllers
│   ├── service/            # Regras de negócio
│   ├── repository/         # Acesso aos dados
│   ├── entity/            # Entidades JPA
│   ├── dto/               # Data Transfer Objects
│   ├── domain/            # Entidades de domínio (Usuario)
│   ├── infra/             # Infraestrutura (Security, Exception)
│   └── config/            # Configurações (Swagger, DataInitializer)
├── src/main/resources/
│   ├── static/            # Frontend (HTML/CSS/JS)
│   └── application.properties
├── *.sql                  # Scripts Oracle
├── GUIA_SWAGGER.html      # Guia completo do Swagger
├── GUIA_COMPLETO_JWT.html # Guia completo do JWT
└── README.md              # Este arquivo
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch: `git checkout -b feature/nova-funcionalidade`
3. Commit suas mudanças: `git commit -m 'Adiciona nova funcionalidade'`
4. Push para a branch: `git push origin feature/nova-funcionalidade`
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👨‍💻 Autor

**Guilherme** - Desenvolvedor na Dataprev

- 📧 Email: [seu.email@exemplo.com]
- 💼 LinkedIn: [seu-linkedin]
- 🐙 GitHub: [seu-github]

---

⭐ **Se este projeto foi útil, deixe uma estrela!** ⭐