package com.example.authnuzhat.repository;

import com.example.authnuzhat.models.Role;
import com.example.authnuzhat.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<User, Long> {
    Optional<Role> findByName(String name);
}
