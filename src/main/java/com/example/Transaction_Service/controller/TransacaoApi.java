package com.example.Transaction_Service.controller;

import com.example.Transaction_Service.dto.TransacaoRequest;
import com.example.Transaction_Service.dto.TransacaoResponse;
import com.example.Transaction_Service.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoApi {

    private final TransactionService transactionService;

    public TransacaoApi(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<String> createTransaction(@RequestBody TransacaoRequest request){
        TransacaoResponse response = transactionService.processTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transação criada: "+ response);
    }
}
