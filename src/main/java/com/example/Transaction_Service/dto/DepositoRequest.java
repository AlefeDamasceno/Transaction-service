package com.example.Transaction_Service.dto;

public record DepositoRequest(
        String numeroConta,
        long valor
) {}
