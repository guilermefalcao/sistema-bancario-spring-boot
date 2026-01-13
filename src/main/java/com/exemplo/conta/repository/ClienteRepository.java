package com.exemplo.conta.repository;

import com.exemplo.conta.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA para Cliente
 * Fornece operações CRUD automáticas para a tabela CLIENTE
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Método para buscar cliente por CPF
    Cliente findByCpf(String cpf);
    
    // Método para verificar se CPF já existe
    boolean existsByCpf(String cpf);
}