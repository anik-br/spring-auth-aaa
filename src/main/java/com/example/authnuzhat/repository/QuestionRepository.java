package com.example.authnuzhat.repository;

import com.example.authnuzhat.models.Question;
import com.example.authnuzhat.models.QuestionImportance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// here giving repository tag is optional, no issue if we don't give
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Find all questions by subject ID
    List<Question> findBySubjectId(Long subjectId);
    // Find all questions by class level ID
    List<Question> findByClassLevelId(Long classLevelId);
    // Find all questions by difficulty level ID
    List<Question> findByDifficultyLevelId(Long difficultyLevelId);
    // Find all questions by question type ID
    List<Question> findByQuestionTypeId(Long questionTypeId);
    List<Question> findByImportance(QuestionImportance importance);
    // Find all active questions
    List<Question> findByActiveTrue();
    List<Question> findByCreatedBy(String createdBy);
    List<Question> findByModifiedBy(String modifiedBy);
    List<Question> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Question> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Question> findByDeletedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    boolean existsBySubjectId(Long id);
}