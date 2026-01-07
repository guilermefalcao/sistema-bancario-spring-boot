@echo off
echo ========================================
echo TESTANDO API DE CONTAS BANCARIAS
echo ========================================
echo.

echo Aguardando servidor iniciar...
timeout /t 5 /nobreak > nul

echo 1. TESTANDO GET /contas (Listar todas as contas)
curl -X GET http://localhost:8080/contas -H "Content-Type: application/json"
echo.
echo.

echo 2. TESTANDO POST /contas (Criar nova conta)
curl -X POST http://localhost:8080/contas -H "Content-Type: application/json" -d "{\"titular\":\"Teste API\",\"saldo\":500.00}"
echo.
echo.

echo 3. TESTANDO GET /contas/1 (Buscar conta por ID)
curl -X GET http://localhost:8080/contas/1 -H "Content-Type: application/json"
echo.
echo.

echo 4. TESTANDO PUT /contas/1 (Atualizar conta completa)
curl -X PUT http://localhost:8080/contas/1 -H "Content-Type: application/json" -d "{\"titular\":\"Teste Atualizado\",\"saldo\":750.00}"
echo.
echo.

echo 5. TESTANDO PATCH /contas/1 (Atualizar apenas saldo)
curl -X PATCH http://localhost:8080/contas/1 -H "Content-Type: application/json" -d "{\"saldo\":1000.00}"
echo.
echo.

echo 6. TESTANDO GET /contas (Verificar alteracoes)
curl -X GET http://localhost:8080/contas -H "Content-Type: application/json"
echo.
echo.

echo ========================================
echo TESTES CONCLUIDOS!
echo ========================================
pause