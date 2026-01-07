package com.exemplo.conta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para operações de movimentação (saque/depósito)
 */
public class MovimentacaoDTO {

    @NotBlank(message = "Tipo é obrigatório (SAQUE ou DEPOSITO)")
    private String tipo;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private Double valor;

    // Construtor padrão
    public MovimentacaoDTO() {}

    // Construtor com parâmetros
    public MovimentacaoDTO(String tipo, Double valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    // Getters e Setters
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

    @Override
    public String toString() {
        return "MovimentacaoDTO{" +
                "tipo='" + tipo + '\'' +
                ", valor=" + valor +
                '}';
    }
}