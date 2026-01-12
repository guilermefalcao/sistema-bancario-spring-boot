package com.exemplo.conta.dto;

public class ContaPatchDTO {

    private String titular;
    private Double saldo;

    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }

    public Double getSaldo() { return saldo; }
    public void setSaldo(Double saldo) { this.saldo = saldo; }
}


//este dto serve para atualizações parciais da conta
