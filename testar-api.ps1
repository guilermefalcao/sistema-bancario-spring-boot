# Script PowerShell para testar API de Contas
# Execute: .\testar-api.ps1

Write-Host "🚀 INICIANDO TESTES DA API DE CONTAS" -ForegroundColor Green
Write-Host "======================================" -ForegroundColor Yellow

# Remove alias do curl se existir
Remove-Item alias:curl -ErrorAction SilentlyContinue

Write-Host "`n📋 1. LISTANDO CONTAS INICIAIS" -ForegroundColor Cyan
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/contas" -Method GET -ContentType "application/json"
    Write-Host $response.Content -ForegroundColor White
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}

Write-Host "`n💰 2. CRIANDO CONTA - João Silva" -ForegroundColor Cyan
try {
    $body = '{"titular":"João Silva","saldo":1500.00}'
    $response = Invoke-WebRequest -Uri "http://localhost:8080/contas" -Method POST -ContentType "application/json" -Body $body
    Write-Host $response.Content -ForegroundColor White
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}

Write-Host "`n💰 3. CRIANDO CONTA - Maria Santos" -ForegroundColor Cyan
try {
    $body = '{"titular":"Maria Santos","saldo":2500.50}'
    $response = Invoke-WebRequest -Uri "http://localhost:8080/contas" -Method POST -ContentType "application/json" -Body $body
    Write-Host $response.Content -ForegroundColor White
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}

Write-Host "`n📋 4. LISTANDO TODAS AS CONTAS" -ForegroundColor Cyan
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/contas" -Method GET -ContentType "application/json"
    Write-Host $response.Content -ForegroundColor White
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}

Write-Host "`n🔍 5. BUSCANDO CONTA ID=1" -ForegroundColor Cyan
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/contas/1" -Method GET -ContentType "application/json"
    Write-Host $response.Content -ForegroundColor White
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}

Write-Host "`n✏️ 6. ATUALIZANDO CONTA COMPLETA (PUT)" -ForegroundColor Cyan
try {
    $body = '{"titular":"João Silva Atualizado","saldo":2000.00}'
    $response = Invoke-WebRequest -Uri "http://localhost:8080/contas/1" -Method PUT -ContentType "application/json" -Body $body
    Write-Host $response.Content -ForegroundColor White
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}

Write-Host "`n🔧 7. ATUALIZANDO APENAS SALDO (PATCH)" -ForegroundColor Cyan
try {
    $body = '{"saldo":3000.00}'
    $response = Invoke-WebRequest -Uri "http://localhost:8080/contas/1" -Method PATCH -ContentType "application/json" -Body $body
    Write-Host $response.Content -ForegroundColor White
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}

Write-Host "`n📋 8. VERIFICANDO ALTERAÇÕES" -ForegroundColor Cyan
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/contas" -Method GET -ContentType "application/json"
    Write-Host $response.Content -ForegroundColor White
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}

Write-Host "`n✅ TESTES CONCLUÍDOS!" -ForegroundColor Green