@echo off
echo ========================================
echo TESTANDO API JWT - Sistema de Contas
echo ========================================

echo.
echo 1. Testando LOGIN...
curl -X POST http://localhost:8080/login ^
  -H "Content-Type: application/json" ^
  -d "{\"login\":\"admin\",\"senha\":\"123456\"}" ^
  -w "\nStatus: %%{http_code}\n"

echo.
echo ========================================
echo 2. Testando CONTAS (sem token - deve dar 401)...
curl -X GET http://localhost:8080/contas ^
  -w "\nStatus: %%{http_code}\n"

echo.
echo ========================================
echo 3. Para testar com token, copie o token do login acima
echo    e execute: 
echo    curl -X GET http://localhost:8080/contas -H "Authorization: Bearer SEU_TOKEN"
echo ========================================

pause