package ru.oorzhak.accountservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.oorzhak.accountservice.dto.AccountRegisterDTO;
import ru.oorzhak.accountservice.model.Account;
import ru.oorzhak.accountservice.model.AccountResponseDTO;
import ru.oorzhak.accountservice.model.Role;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
//@EnableDiscoveryClient
public class AccountServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}
}
