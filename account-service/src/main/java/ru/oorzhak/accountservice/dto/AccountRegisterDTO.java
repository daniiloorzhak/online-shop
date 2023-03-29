package ru.oorzhak.accountservice.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRegisterDTO {
    @NotBlank
    @Size(max = 255)
    private String username;

    @NotBlank
    @Size(max = 511)
    private String password;

    @NotBlank
    @Size(max = 511)
    private String confirmPassword;

    @Email
    @NotNull
    @Size(max = 255)
    private String email;
}
