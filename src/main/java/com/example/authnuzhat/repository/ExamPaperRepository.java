package com.example.authnuzhat.repository;

import com.example.authnuzhat.models.ExamPaper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamPaperRepository extends JpaRepository<ExamPaper, Long> {
    List<ExamPaper> findByCreatedBy(String createdBy);

}