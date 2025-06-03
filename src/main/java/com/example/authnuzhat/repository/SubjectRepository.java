package com.example.authnuzhat.repository;

import com.example.authnuzhat.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByName(String name); // Check if a subject exists by name
    List<Subject> findByParentIsNull(); // Find all subjects with no parent (root subjects)
}