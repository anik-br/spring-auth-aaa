package com.example.authnuzhat.repository;

import com.example.authnuzhat.models.McqOptions;
import com.example.authnuzhat.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface McqOptionsRepository extends JpaRepository<McqOptions, Long> {
    List<McqOptions> findByQuestion(Question question); // Find options by question
    List<McqOptions> findByQuestionAndCorrect(Question question, boolean correct); // Find all correct options for a specific mcq question

    List<McqOptions> findByQuestionId(Long questionId);

    void deleteByQuestionId(Long questionId);
}