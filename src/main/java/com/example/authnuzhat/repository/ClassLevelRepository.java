package com.example.authnuzhat.repository;

import com.example.authnuzhat.models.ClassLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassLevelRepository extends JpaRepository<ClassLevel, Long> {
    // Find by name (useful for validation)
    boolean existsByName(String name);
}