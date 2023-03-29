package ru.oorzhak.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.oorzhak.accountservice.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByUsername(String username);
    Optional<Account> findAccountByEmail(String email);
    Optional<Account> findAccountById(Long id);
}
