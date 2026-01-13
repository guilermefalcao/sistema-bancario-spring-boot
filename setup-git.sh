#!/bin/bash

echo "🚀 Iniciando configuração Git..."

echo ""
echo "📋 PASSO 1: Atualizando branch main"
echo "=================================="

# Verificar status
echo "1. Verificando status..."
git status

# Adicionar .gitignore
echo "2. Adicionando .gitignore..."
git add .gitignore

# Commit
echo "3. Fazendo commit..."
git commit -m "feat: adiciona .env ao .gitignore para proteger configurações sensíveis"

# Push para main
echo "4. Enviando para main..."
git push origin main

echo ""
echo "📋 PASSO 2: Criando branch desenvolvimentoTeste"
echo "=============================================="

# Criar nova branch
echo "1. Criando nova branch..."
git checkout -b desenvolvimentoTeste

# Verificar branch atual
echo "2. Verificando branch atual..."
git branch

# Adicionar .env
echo "3. Adicionando .env..."
git add .env

# Commit na nova branch
echo "4. Fazendo commit na nova branch..."
git commit -m "feat: adiciona arquivo .env com configurações de desenvolvimento"

# Push da nova branch
echo "5. Enviando nova branch..."
git push -u origin desenvolvimentoTeste

# Verificar todas as branches
echo "6. Verificando todas as branches..."
git branch -a

echo ""
echo "✅ Configuração concluída!"
echo "📍 Você está agora na branch: desenvolvimentoTeste"