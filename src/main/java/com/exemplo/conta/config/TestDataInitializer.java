package com.exemplo.conta.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.exemplo.conta.entity.Cliente;
import com.exemplo.conta.entity.Conta;
import com.exemplo.conta.repository.ClienteRepository;
import com.exemplo.conta.repository.ContaRepository;

import java.sql.Timestamp;

/**
 * Classe para inserir dados de teste (clientes e contas)
 * Executa após o DataInitializer (usuários)
 */
@Component
@Order(2) // Executa depois do DataInitializer
public class TestDataInitializer implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Override
    public void run(String... args) throws Exception {
        
        System.out.println("🏦 Inicializando dados de teste (clientes e contas)...");
        
        // Verifica se já existem clientes
        long totalClientes = clienteRepository.count();
        
        if (totalClientes == 0) {
            System.out.println("📝 Criando clientes e contas de teste...");
            
            // Criar clientes e contas de teste
            criarClienteEConta("João Silva", "12345678901", 1500.00);
            criarClienteEConta("Maria Santos", "98765432100", 2300.50);
            criarClienteEConta("Pedro Oliveira", "11122233344", 850.75);
            criarClienteEConta("Ana Costa", "55566677788", 3200.00);
            
            System.out.println("✅ Dados de teste criados com sucesso!");
            
        } else {
            System.out.println("ℹ️ Dados de teste já existem (" + totalClientes + " clientes)");
        }
        
        // Lista dados para verificação
        listarDados();
    }
    
    /**
     * Cria um cliente e sua conta correspondente
     */
    private void criarClienteEConta(String nome, String cpf, double saldoInicial) {
        try {
            // Verificar se CPF já existe
            if (clienteRepository.existsByCpf(cpf)) {
                System.out.println("⚠️ Cliente com CPF '" + cpf + "' já existe, pulando...");
                return;
            }
            
            // Criar cliente
            Cliente cliente = new Cliente();
            cliente.setNome(nome);
            cliente.setCpf(cpf);
            cliente.setDataCadastro(new Timestamp(System.currentTimeMillis()));
            cliente = clienteRepository.save(cliente);
            
            // Criar conta para o cliente
            Conta conta = new Conta();
            conta.setIdCliente(cliente.getId());
            conta.setSaldo(saldoInicial);
            conta = contaRepository.save(conta);
            
            System.out.println("👤 Cliente criado: " + nome + " (CPF: " + cpf + ") - Conta ID: " + conta.getId() + " - Saldo: R$ " + saldoInicial);
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar cliente '" + nome + "': " + e.getMessage());
        }
    }
    
    /**
     * Lista todos os dados para verificação
     */
    private void listarDados() {
        try {
            var clientes = clienteRepository.findAll();
            var contas = contaRepository.findAll();
            
            System.out.println("\n📋 Dados no sistema:");
            System.out.println("====================");
            System.out.println("👥 Clientes: " + clientes.size());
            System.out.println("🏦 Contas: " + contas.size());
            
            if (!contas.isEmpty()) {
                System.out.println("\n💰 Contas cadastradas:");
                for (Conta conta : contas) {
                    Cliente cliente = clienteRepository.findById(conta.getIdCliente()).orElse(null);
                    String nomeCliente = cliente != null ? cliente.getNome() : "Cliente não encontrado";
                    System.out.println("  ID: " + conta.getId() + " | " + nomeCliente + " | Saldo: R$ " + String.format("%.2f", conta.getSaldo()));
                }
            }
            
            System.out.println("====================\n");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao listar dados: " + e.getMessage());
        }
    }
}