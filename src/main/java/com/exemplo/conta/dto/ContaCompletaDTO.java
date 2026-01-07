package com.exemplo.conta.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para criar Cliente + Conta em uma única operação
 */
public class ContaCompletaDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
    private String cpf;

    @NotNull(message = "Saldo é obrigatório")
    @Min(value = 0, message = "Saldo não pode ser negativo")
    private Double saldo;

    // Construtor padrão
    public ContaCompletaDTO() {}

    // Construtor com parâmetros
    public ContaCompletaDTO(String nome, String cpf, Double saldo) {
        this.nome = nome;
        this.cpf = cpf;
        this.saldo = saldo;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "ContaCompletaDTO{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}