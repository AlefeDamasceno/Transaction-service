package com.example.Transaction_Service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Account {

    @Id
    private UUID id;

    private String numeroConta;

    private long saldo;

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public long getSaldo() {
        return saldo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setSaldo(long saldo) {
        this.saldo = saldo;
    }
}
