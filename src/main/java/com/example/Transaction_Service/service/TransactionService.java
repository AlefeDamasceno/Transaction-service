package com.example.Transaction_Service.service;

import com.example.Transaction_Service.Enum.StatusTransacao;
import com.example.Transaction_Service.Enum.TipoTransacao;
import com.example.Transaction_Service.dto.DepositoRequest;
import com.example.Transaction_Service.dto.TransacaoRequest;
import com.example.Transaction_Service.dto.TransacaoResponse;
import com.example.Transaction_Service.entity.Account;
import com.example.Transaction_Service.exception.AccountNotFoundException;
import com.example.Transaction_Service.exception.InvalidDepositAmountException;
import com.example.Transaction_Service.producer.TransactionProducer;
import com.example.Transaction_Service.repository.AccountRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private final RedisTemplate<String, Long> redisTemplate;

    private final TransactionProducer transactionProducer;

    private final AccountRepository accountRepository;

    public TransactionService(RedisTemplate<String, Long> redisTemplate,
                              TransactionProducer transactionProducer, AccountRepository accountRepository) {
        this.redisTemplate = redisTemplate;
        this.transactionProducer = transactionProducer;
        this.accountRepository = accountRepository;
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

    public void depositar(DepositoRequest request){
        String numeroConta = request.numeroConta();
        long valor = request.valor();

        Account account = validarContaExistente(numeroConta);

        if (valor > 0 && valor <= account.getSaldo()){
            account.setSaldo(account.getSaldo() + valor);

            accountRepository.save(account);

        } else {
            throw new InvalidDepositAmountException();
        }
    }

    private Account validarContaExistente(String numeroConta){
        Optional<Account> contaValidada = accountRepository.findByNumeroConta(numeroConta);

        if (contaValidada.isEmpty()){
            throw new AccountNotFoundException();
        }

        return contaValidada.get();
    }
}