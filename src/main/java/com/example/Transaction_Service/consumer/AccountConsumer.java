package com.example.Transaction_Service.consumer;

import com.example.Transaction_Service.dto.event.AccountEvent;
import com.example.Transaction_Service.entity.Account;
import com.example.Transaction_Service.mapper.AccountMapper;
import com.example.Transaction_Service.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class AccountConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AccountConsumer.class);


    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    public AccountConsumer(AccountMapper accountMapper, AccountRepository accountRepository) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
    }

    @Bean
    public Consumer<AccountEvent> accountEventConsumer(){
        return event -> {
            logger.info("[AccountEventConsumer] - Evento recebido via kafka");

            Account entity = accountMapper.toEntity(event);

            accountRepository.save(entity); //TODO Service persistir entidade
        };
    }
}
