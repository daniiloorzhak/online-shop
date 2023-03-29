package ru.oorzhak.accountservice.service;

import ru.oorzhak.accountservice.dto.AccountRegisterDTO;
import ru.oorzhak.accountservice.model.Account;

public interface AccountService {
    Account save(AccountRegisterDTO accountRegisterDTO);
    Account findByUsername(String username);
}
