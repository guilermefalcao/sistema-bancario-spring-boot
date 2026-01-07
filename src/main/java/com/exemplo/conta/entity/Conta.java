package com.exemplo.conta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Entidade JPA que representa uma Conta bancária
 * Mapeada para a tabela CONTA no Oracle
 */
@Entity
@Table(name = "CONTA")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conta_seq")
    @SequenceGenerator(name = "conta_seq", sequenceName = "SEQ_CONTA", allocationSize = 1)
    @Column(name = "ID_CONTA")
    private Long id;

    @Column(name = "ID_CLIENTE", nullable = false)
    private Long idCliente;

    @Column(name = "SALDO", nullable = false)
    @NotNull(message = "Saldo é obrigatório")
    @Min(value = 0, message = "Saldo não pode ser negativo")
    private Double saldo;

    // Para compatibilidade com a interface web, vamos usar um campo virtual
    @Transient
    private String titular;

    // Construtor padrão (obrigatório para JPA)
    public Conta() {}

    // Construtor com parâmetros
    public Conta(Long idCliente, Double saldo) {
        this.idCliente = idCliente;
        this.saldo = saldo;
    }

    // Construtor para compatibilidade com titular
    public Conta(String titular, Double saldo) {
        this.titular = titular;
        this.saldo = saldo;
        this.idCliente = 1L; // Cliente padrão para testes
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Conta{" +
                "id=" + id +
                ", idCliente=" + idCliente +
                ", titular='" + titular + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}
