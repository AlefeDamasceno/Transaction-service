package com.example.Transaction_Service.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException() {
        super("Conta não existente.");
    }
}
