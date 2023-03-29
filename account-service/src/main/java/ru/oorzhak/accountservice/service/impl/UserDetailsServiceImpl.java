package ru.oorzhak.accountservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.oorzhak.accountservice.model.Account;
import ru.oorzhak.accountservice.repository.AccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Autowired
    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username).orElseThrow();
        return new UserDetailsImpl(account);
    }
}
