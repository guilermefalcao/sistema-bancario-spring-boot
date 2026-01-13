#!/bin/bash
# Script de teste completo da API
# Execute: bash testar-completo.sh

echo "🚀 INICIANDO TESTES DA API DE CONTAS"
echo "======================================"

echo "📋 1. LISTANDO CONTAS INICIAIS"
curl -s -X GET http://localhost:8080/contas | echo "$(cat)" | python -m json.tool 2>/dev/null || echo "$(cat)"
echo -e "\n"

echo "💰 2. CRIANDO CONTA - João Silva"
curl -s -X POST http://localhost:8080/contas -H "Content-Type: application/json" -d '{"titular":"João Silva","saldo":1500.00}' | python -m json.tool 2>/dev/null || echo "$(cat)"
echo -e "\n"

echo "💰 3. CRIANDO CONTA - Maria Santos"  
curl -s -X POST http://localhost:8080/contas -H "Content-Type: application/json" -d '{"titular":"Maria Santos","saldo":2500.50}' | python -m json.tool 2>/dev/null || echo "$(cat)"
echo -e "\n"

echo "📋 4. LISTANDO TODAS AS CONTAS"
curl -s -X GET http://localhost:8080/contas | python -m json.tool 2>/dev/null || echo "$(cat)"
echo -e "\n"

echo "🔍 5. BUSCANDO CONTA ID=1"
curl -s -X GET http://localhost:8080/contas/1 | python -m json.tool 2>/dev/null || echo "$(cat)"
echo -e "\n"

echo "✏️ 6. ATUALIZANDO CONTA COMPLETA (PUT)"
curl -s -X PUT http://localhost:8080/contas/1 -H "Content-Type: application/json" -d '{"titular":"João Silva Atualizado","saldo":2000.00}' | python -m json.tool 2>/dev/null || echo "$(cat)"
echo -e "\n"

echo "🔧 7. ATUALIZANDO APENAS SALDO (PATCH)"
curl -s -X PATCH http://localhost:8080/contas/1 -H "Content-Type: application/json" -d '{"saldo":3000.00}' | python -m json.tool 2>/dev/null || echo "$(cat)"
echo -e "\n"

echo "📋 8. VERIFICANDO ALTERAÇÕES"
curl -s -X GET http://localhost:8080/contas | python -m json.tool 2>/dev/null || echo "$(cat)"
echo -e "\n"

echo "🗑️ 9. EXCLUINDO CONTA ID=2"
curl -s -X DELETE http://localhost:8080/contas/2
echo "Conta excluída"
echo -e "\n"

echo "📋 10. LISTAGEM FINAL"
curl -s -X GET http://localhost:8080/contas | python -m json.tool 2>/dev/null || echo "$(cat)"
echo -e "\n"

echo "✅ TESTES CONCLUÍDOS!"