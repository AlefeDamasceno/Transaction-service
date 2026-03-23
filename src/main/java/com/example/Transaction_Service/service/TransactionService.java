package com.example.Transaction_Service.service;

import com.example.Transaction_Service.Enum.StatusTransacao;
import com.example.Transaction_Service.Enum.TipoTransacao;
import com.example.Transaction_Service.dto.TransacaoRequest;
import com.example.Transaction_Service.dto.TransacaoResponse;
import com.example.Transaction_Service.producer.TransactionProducer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService {

    private final RedisTemplate<String, Long> redisTemplate;
    private final TransactionProducer transactionProducer;

    public TransactionService(RedisTemplate<String, Long> redisTemplate,
                              TransactionProducer transactionProducer) {
        this.redisTemplate = redisTemplate;
        this.transactionProducer = transactionProducer;
    }

    public TransacaoResponse processTransaction(TransacaoRequest request){

        if (request.valor() == null || request.valor() <= 0) {
            throw new IllegalArgumentException("Valor da transação deve ser maior que zero");
        }

        UUID id = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();

        StatusTransacao statusTransacao = validateSaldoRedis(request) ?
                StatusTransacao.APROVADA : StatusTransacao.REPROVADA;

        TransacaoResponse transacaoResponse = new TransacaoResponse(
                id,
                request.contaId(),
                request.valor(),
                request.tipoTransacao(),
                statusTransacao,
                timestamp
        );

        transactionProducer.sendTransaction(transacaoResponse);

        return transacaoResponse;
    }

    private boolean validateSaldoRedis(TransacaoRequest request) {

        if (request.tipoTransacao().equals(TipoTransacao.DEBITO)) {
            String chaveSaldo = "saldo:usuario:" + request.contaId();
            long saldoAtual = obterValorRedis(chaveSaldo);

            if (saldoAtual >= request.valor()) {
                redisTemplate.opsForValue().decrement(chaveSaldo, request.valor());
                return true;
            }
        }

        String chaveLimite = "limite:usuario:" + request.contaId();
        long limiteAtual = obterValorRedis(chaveLimite);

        if (limiteAtual >= request.valor()) {
            redisTemplate.opsForValue().decrement(chaveLimite, request.valor());
            return true;
        }

        return false;
    }

    private long obterValorRedis(String chave) {
        Object valor = redisTemplate.opsForValue().get(chave);
        return valor != null ? Long.parseLong(valor.toString()) : 0L;
    }
}