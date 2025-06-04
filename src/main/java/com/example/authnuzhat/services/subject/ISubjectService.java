package com.example.authnuzhat.services.subject;

import com.example.authnuzhat.dto.request.SubjectRequestDTO;
import com.example.authnuzhat.dto.response.SubjectResponseDTO;

import java.util.List;

public interface ISubjectService {
    // Create a new Subject
    SubjectResponseDTO createSubject(SubjectRequestDTO subjectRequestDTO);

    // Get all Subjects
    List<SubjectResponseDTO> getAllSubjects();

    // Get a Subject by ID
    SubjectResponseDTO getSubjectById(Long id);

    // Update a Subject
    SubjectResponseDTO updateSubject(Long id, SubjectRequestDTO subjectRequestDTO);

    // Delete a Subject
    void deleteSubject(Long id);

    // Get all root subjects (subjects with no parent)
    List<SubjectResponseDTO> getAllRootSubjects();
}