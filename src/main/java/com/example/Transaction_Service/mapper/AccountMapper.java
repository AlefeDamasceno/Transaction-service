package com.example.Transaction_Service.mapper;

import com.example.Transaction_Service.dto.event.AccountEvent;
import com.example.Transaction_Service.entity.Account;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountMapper {

    public Account toEntity(AccountEvent event){
        Account account = new Account();

        account.setId(UUID.randomUUID());
        account.setNumeroConta(event.numeroConta());
        account.setSaldo(event.saldo());

        return account;
    }
}
