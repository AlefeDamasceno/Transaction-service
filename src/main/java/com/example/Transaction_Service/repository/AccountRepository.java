package com.example.Transaction_Service.repository;

import com.example.Transaction_Service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByNumeroConta(String numeroConta);
}
