package ru.oorzhak.accountservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account")
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    @Size(max = 255)
    private String username;

    @NotBlank
    @Size(max = 511)
    private String password;

    @Email
    @NotNull
    @Column(unique = true)
    @Size(max = 255)
    private String email;

    @Min(0)
    @NotNull
    private Long balance;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
