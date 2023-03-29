package ru.oorzhak.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.oorzhak.accountservice.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
