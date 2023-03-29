package ru.oorzhak.accountservice.configuration;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.oorzhak.accountservice.dto.AccountRegisterDTO;
import ru.oorzhak.accountservice.model.Account;
import ru.oorzhak.accountservice.model.AccountResponseDTO;
import ru.oorzhak.accountservice.model.Role;
import ru.oorzhak.accountservice.repository.RoleRepository;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ApplicationConfig {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public ApplicationConfig(RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public ModelMapper modelMapper () {

        Converter<Set<Role>, Set<String>> converter = ctx -> {
            return ctx.getSource().stream().map(Role::getName).collect(Collectors.toSet());
        };

        var modelMapper = new ModelMapper();
//        modelMapper.typeMap(Account.class, AccountResponseDTO.class).addMappings(mapper -> {
//            mapper.map(src -> src.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
//                    AccountResponseDTO::setRoles);
//        });

        modelMapper.typeMap(Account.class, AccountResponseDTO.class).addMappings(mapping -> {
            mapping.using(converter).map(Account::getRoles, AccountResponseDTO::setRoles);
        });

        modelMapper.typeMap(AccountRegisterDTO.class, Account.class).addMappings(mapper -> {
            mapper.map(src -> {
                Role role = roleRepository.findByName("ROLE_USER").orElseThrow();
                return Collections.singleton(role);
            }, Account::setRoles);
            mapper.map(src -> 0L, Account::setBalance);
            mapper.using(context -> passwordEncoder.encode((String) context.getSource())).map(AccountRegisterDTO::getPassword, Account::setPassword);
        });

        return modelMapper;
    }
}
