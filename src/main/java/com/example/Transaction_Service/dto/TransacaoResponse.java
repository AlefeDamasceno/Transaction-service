package com.example.Transaction_Service.dto;

import com.example.Transaction_Service.Enum.StatusTransacao;
import com.example.Transaction_Service.Enum.TipoTransacao;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransacaoResponse(
        UUID id,
        String contaId,
        Long valor,
        TipoTransacao tipoTransacao,
        StatusTransacao status,
        LocalDateTime timestamp
){}
