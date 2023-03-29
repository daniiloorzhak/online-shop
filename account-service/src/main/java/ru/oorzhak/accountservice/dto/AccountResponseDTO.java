package ru.oorzhak.accountservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AccountResponseDTO {
    private Long id;

    private String username;

    private String email;

    private Long balance;

    private Boolean isFrozen;

    private Set<String> roles;
}
