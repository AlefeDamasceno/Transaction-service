package com.example.Transaction_Service.exception;

public class InvalidDepositAmountException extends RuntimeException {
    public InvalidDepositAmountException() { super("Conta não existente.");
    }
}
