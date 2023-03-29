package ru.oorzhak.accountservice.service.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.oorzhak.accountservice.dto.AccountRegisterDTO;
import ru.oorzhak.accountservice.model.Account;
import ru.oorzhak.accountservice.repository.AccountRepository;
import ru.oorzhak.accountservice.repository.RoleRepository;
import ru.oorzhak.accountservice.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public AccountServiceImpl(PasswordEncoder passwordEncoder, AccountRepository accountRepository, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Account save(AccountRegisterDTO accountRegisterDTO) {
        if (!accountRegisterDTO.getPassword().equals(accountRegisterDTO.getConfirmPassword())) {
            throw new RuntimeException();
        }
        Account account = modelMapper.map(accountRegisterDTO, Account.class);
        return accountRepository.save(account);
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findAccountByUsername(username).orElseThrow();
    }
}
