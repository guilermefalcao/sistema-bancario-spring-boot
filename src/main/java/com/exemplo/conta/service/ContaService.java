package com.exemplo.conta.service;

import com.exemplo.conta.entity.Conta;
import com.exemplo.conta.entity.Cliente;
import com.exemplo.conta.entity.Movimentacao;
import com.exemplo.conta.repository.ContaRepository;
import com.exemplo.conta.repository.ClienteRepository;
import com.exemplo.conta.repository.MovimentacaoRepository;
import com.exemplo.conta.dto.ContaCompletaDTO;
import com.exemplo.conta.dto.MovimentacaoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    // Injeção de dependência via construtor
    public ContaService(ContaRepository contaRepository, ClienteRepository clienteRepository, MovimentacaoRepository movimentacaoRepository) {
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    // LISTAR TODAS AS CONTAS COM DADOS DO CLIENTE
    public List<Conta> listar() {
        List<Conta> contas = contaRepository.findAll();
        // Para cada conta, buscar dados do cliente
        for (Conta conta : contas) {
            Cliente cliente = clienteRepository.findById(conta.getIdCliente()).orElse(null);
            if (cliente != null) {
                conta.setTitular(cliente.getNome() + " (CPF: " + cliente.getCpf() + ")");
            } else {
                conta.setTitular("Cliente não encontrado");
            }
        }
        return contas;
    }

    // BUSCAR POR ID COM DADOS DO CLIENTE
    public Conta buscarPorId(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        
        // Buscar dados do cliente
        Cliente cliente = clienteRepository.findById(conta.getIdCliente()).orElse(null);
        if (cliente != null) {
            conta.setTitular(cliente.getNome() + " (CPF: " + cliente.getCpf() + ")");
        }
        return conta;
    }

    // CRIAR CONTA COMPLETA (CLIENTE + CONTA)
    public Conta criarContaCompleta(ContaCompletaDTO dto) {
        // Verificar se CPF já existe
        if (clienteRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + dto.getCpf());
        }
        
        // Criar cliente
        Cliente cliente = new Cliente(dto.getNome(), dto.getCpf());
        cliente.setDataCadastro(new java.sql.Timestamp(System.currentTimeMillis()));
        cliente = clienteRepository.save(cliente);
        
        // Criar conta
        Conta conta = new Conta();
        conta.setIdCliente(cliente.getId());
        conta.setSaldo(dto.getSaldo());
        conta.setTitular(cliente.getNome() + " (CPF: " + cliente.getCpf() + ")");
        
        return contaRepository.save(conta);
    }

    // EXCLUIR CONTA E CLIENTE
    @Transactional
    public void excluir(Long id) {
        // Buscar a conta
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada para exclusão"));
        
        Long idCliente = conta.getIdCliente();
        
        // Excluir movimentações primeiro (devido à chave estrangeira)
        movimentacaoRepository.deleteByIdConta(id);
        
        // Excluir conta
        contaRepository.deleteById(id);
        
        // Excluir cliente
        if (idCliente != null) {
            try {
                clienteRepository.deleteById(idCliente);
            } catch (Exception e) {
                // Se não conseguir excluir cliente, apenas log
                System.out.println("Não foi possível excluir cliente: " + e.getMessage());
            }
        }
    }

    // ATUALIZAÇÃO COMPLETA (PUT) - ATUALIZA CLIENTE E CONTA
    @Transactional
    public Conta atualizar(Long id, Conta contaAtualizada) {
        Conta contaExistente = contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada para atualização"));

        // Atualizar saldo da conta
        contaExistente.setSaldo(contaAtualizada.getSaldo());
        
        // Se foi informado titular, atualizar nome do cliente
        if (contaAtualizada.getTitular() != null && !contaAtualizada.getTitular().trim().isEmpty()) {
            Cliente cliente = clienteRepository.findById(contaExistente.getIdCliente()).orElse(null);
            if (cliente != null) {
                // Extrair apenas o nome (remover CPF se estiver junto)
                String novoNome = contaAtualizada.getTitular();
                if (novoNome.contains("(CPF:")) {
                    novoNome = novoNome.substring(0, novoNome.indexOf("(CPF:")).trim();
                }
                cliente.setNome(novoNome);
                clienteRepository.save(cliente);
                
                // Atualizar titular na conta para exibição
                contaExistente.setTitular(cliente.getNome() + " (CPF: " + cliente.getCpf() + ")");
            }
        }

        return contaRepository.save(contaExistente);
    }

    // ATUALIZAÇÃO PARCIAL (PATCH) - ATUALIZA CLIENTE E CONTA
    @Transactional
    public Conta atualizarParcial(Long id, Map<String, Object> updates) {
        Conta contaExistente = contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada para atualização parcial"));

        // Atualizar saldo se informado
        if (updates.containsKey("saldo")) {
            Object saldoObj = updates.get("saldo");
            if (saldoObj instanceof Number) {
                contaExistente.setSaldo(((Number) saldoObj).doubleValue());
            } else {
                throw new RuntimeException("Saldo inválido");
            }
        }
        
        // Atualizar nome do cliente se informado
        if (updates.containsKey("titular")) {
            String novoTitular = (String) updates.get("titular");
            if (novoTitular != null && !novoTitular.trim().isEmpty()) {
                Cliente cliente = clienteRepository.findById(contaExistente.getIdCliente()).orElse(null);
                if (cliente != null) {
                    // Extrair apenas o nome (remover CPF se estiver junto)
                    String novoNome = novoTitular;
                    if (novoNome.contains("(CPF:")) {
                        novoNome = novoNome.substring(0, novoNome.indexOf("(CPF:")).trim();
                    }
                    cliente.setNome(novoNome);
                    clienteRepository.save(cliente);
                    
                    // Atualizar titular na conta para exibição
                    contaExistente.setTitular(cliente.getNome() + " (CPF: " + cliente.getCpf() + ")");
                }
            }
        }

        return contaRepository.save(contaExistente);
    }

    // BUSCAR EXTRATO DE UMA CONTA
    public List<Movimentacao> buscarExtrato(Long idConta) {
        // Verificar se conta existe
        if (!contaRepository.existsById(idConta)) {
            throw new RuntimeException("Conta não encontrada");
        }
        return movimentacaoRepository.findByIdContaOrderByDataMovimentacaoDesc(idConta);
    }

    // REALIZAR SAQUE
    @Transactional
    public Movimentacao realizarSaque(Long idConta, MovimentacaoDTO movimentacaoDTO) {
        // Buscar conta
        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        
        // Usar o saldo atual da conta (que já inclui saldo inicial + movimentações)
        double saldoAtual = conta.getSaldo();
        
        // Verificar saldo suficiente
        if (saldoAtual < movimentacaoDTO.getValor()) {
            throw new RuntimeException("Saldo insuficiente. Saldo atual: R$ " + String.format("%.2f", saldoAtual));
        }
        
        // Registrar movimentação
        Movimentacao movimentacao = new Movimentacao(idConta, "SAQUE", movimentacaoDTO.getValor());
        movimentacao = movimentacaoRepository.save(movimentacao);
        
        // Atualizar saldo na conta
        conta.setSaldo(saldoAtual - movimentacaoDTO.getValor());
        contaRepository.save(conta);
        
        return movimentacao;
    }

    // REALIZAR DEPÓSITO
    @Transactional
    public Movimentacao realizarDeposito(Long idConta, MovimentacaoDTO movimentacaoDTO) {
        // Buscar conta
        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        
        // Registrar movimentação
        Movimentacao movimentacao = new Movimentacao(idConta, "DEPOSITO", movimentacaoDTO.getValor());
        movimentacao = movimentacaoRepository.save(movimentacao);
        
        // Atualizar saldo na conta
        conta.setSaldo(conta.getSaldo() + movimentacaoDTO.getValor());
        contaRepository.save(conta);
        
        return movimentacao;
    }
}