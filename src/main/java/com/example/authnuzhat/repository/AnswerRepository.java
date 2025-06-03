package com.example.authnuzhat.repository;


import com.example.authnuzhat.models.Answer;
import com.example.authnuzhat.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion(Question question); // Find answers by question

    void deleteByQuestionId(Long questionId);

    Optional<Answer> findByQuestionId(Long questionId);
}