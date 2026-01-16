-- =====================================================
-- Migration V4: População inicial da tabela usuarios
-- =====================================================
-- Esta migration insere usuários padrão do sistema
-- Senhas são criptografadas com BCrypt (senha original: 123456)

-- Inserção do usuário administrador
-- Login: admin | Senha: 123456 (BCrypt hash)
INSERT INTO usuarios (login, senha) VALUES (
    'admin', 
    '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.'
);

-- Inserção do usuário comum
-- Login: usuario | Senha: 123456 (BCrypt hash)
INSERT INTO usuarios (login, senha) VALUES (
    'usuario', 
    '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.'
);

-- Inserção do usuário de teste
-- Login: teste | Senha: 123456 (BCrypt hash)
INSERT INTO usuarios (login, senha) VALUES (
    'teste', 
    '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.'
);

-- Commit das inserções para garantir persistência
COMMIT;

-- Comentário: Hash BCrypt gerado com strength 10
-- Para gerar novos hashes, use: BCryptPasswordEncoder().encode("senha")