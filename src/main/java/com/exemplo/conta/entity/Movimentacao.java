package com.exemplo.conta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.sql.Timestamp;

/**
 * Entidade JPA que representa uma Movimentação bancária
 * Mapeada para a tabela MOVIMENTACAO no Oracle
 */
@Entity
@Table(name = "MOVIMENTACAO")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mov_seq")
    @SequenceGenerator(name = "mov_seq", sequenceName = "SEQ_MOVIMENTACAO", allocationSize = 1)
    @Column(name = "ID_MOV")
    private Long id;

    @Column(name = "ID_CONTA", nullable = false)
    @NotNull(message = "ID da conta é obrigatório")
    private Long idConta;

    @Column(name = "TIPO", nullable = false, length = 20)
    @NotBlank(message = "Tipo é obrigatório")
    private String tipo; // DEPOSITO ou SAQUE

    @Column(name = "VALOR", nullable = false)
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private Double valor;

    @Column(name = "DATA_MOV")
    private Timestamp dataMovimentacao;

    // Construtor padrão
    public Movimentacao() {}

    // Construtor com parâmetros
    public Movimentacao(Long idConta, String tipo, Double valor) {
        this.idConta = idConta;
        this.tipo = tipo;
        this.valor = valor;
        this.dataMovimentacao = new Timestamp(System.currentTimeMillis());
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdConta() {
        return idConta;
    }

    public void setIdConta(Long idConta) {
        this.idConta = idConta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Timestamp getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(Timestamp dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    @Override
    public String toString() {
        return "Movimentacao{" +
                "id=" + id +
                ", idConta=" + idConta +
                ", tipo='" + tipo + '\'' +
                ", valor=" + valor +
                ", dataMovimentacao=" + dataMovimentacao +
                '}';
    }
}