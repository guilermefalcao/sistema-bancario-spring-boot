-- =====================================================
-- SCRIPT PARA INSERIR USUÁRIOS MANUALMENTE
-- =====================================================
-- Execute este script no Oracle SQL Developer ou similar
-- após a aplicação criar as tabelas automaticamente

-- Inserção do usuário administrador
-- Login: admin | Senha: 123456 (BCrypt hash)
INSERT INTO usuarios (id, login, senha) VALUES (
    1,
    'admin', 
    '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.'
);

-- Inserção do usuário comum
-- Login: usuario | Senha: 123456 (BCrypt hash)
INSERT INTO usuarios (id, login, senha) VALUES (
    2,
    'usuario', 
    '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.'
);

-- Inserção do usuário de teste
-- Login: teste | Senha: 123456 (BCrypt hash)
INSERT INTO usuarios (id, login, senha) VALUES (
    3,
    'teste', 
    '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.'
);

-- Commit das inserções
COMMIT;

-- Verificar se os usuários foram inseridos
SELECT * FROM usuarios;

-- Comentário: 
-- Hash BCrypt para senha "123456": $2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.
-- Para gerar novos hashes, use: new BCryptPasswordEncoder().encode("senha")