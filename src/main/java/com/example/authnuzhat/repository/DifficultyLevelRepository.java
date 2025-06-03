package com.example.authnuzhat.repository;

import com.example.authnuzhat.models.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DifficultyLevelRepository extends JpaRepository<DifficultyLevel, Long> {
    boolean existsByLevel(String level); // Check if a difficulty level exists by name

}