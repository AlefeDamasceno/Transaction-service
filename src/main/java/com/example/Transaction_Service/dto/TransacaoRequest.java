package com.example.Transaction_Service.dto;

import com.example.Transaction_Service.Enum.TipoTransacao;

public record TransacaoRequest(
        String contaId,
        Long valor,
        TipoTransacao tipoTransacao
) {}
