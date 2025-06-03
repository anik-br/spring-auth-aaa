package com.example.authnuzhat.repository;

import com.example.authnuzhat.models.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionTypeRepository extends JpaRepository<QuestionType, Long> {
    Optional<QuestionType> findByName(String name); // Find question type by name

    boolean existsByName(String name); // Check if a question type exists by name

}