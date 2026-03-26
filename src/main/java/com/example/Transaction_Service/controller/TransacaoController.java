package com.example.Transaction_Service.controller;

import com.example.Transaction_Service.dto.DepositoRequest;
import com.example.Transaction_Service.dto.TransacaoRequest;
import com.example.Transaction_Service.dto.TransacaoResponse;
import com.example.Transaction_Service.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    private final TransactionService transactionService;

    public TransacaoController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransacaoResponse> createTransaction(@RequestBody TransacaoRequest request){
        TransacaoResponse response = transactionService.processTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/depositar")
    public ResponseEntity<String> depositar(@RequestBody DepositoRequest request){
        transactionService.depositar(request);
        return ResponseEntity.status(HttpStatus.OK).body("Deposito realizado com sucesso!");
    }
}
