package com.example.Transaction_Service.event;

import com.example.Transaction_Service.Enum.StatusTransacao;
import com.example.Transaction_Service.Enum.TipoTransacao;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransacaoEvent(
        UUID id,
        String contaId,
        Long valor,
        TipoTransacao tipoTransacao,
        StatusTransacao status,
        LocalDateTime timestamp
) {}
