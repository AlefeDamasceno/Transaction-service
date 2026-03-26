package com.example.Transaction_Service.dto.event;

public record AccountEvent(
        String numeroConta,
        long saldo
) {}
