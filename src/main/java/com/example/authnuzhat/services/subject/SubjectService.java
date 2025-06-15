package com.example.authnuzhat.services.subject;

import com.example.authnuzhat.dto.request.SubjectRequestDTO;
import com.example.authnuzhat.dto.response.ChildSubjectDTO;
import com.example.authnuzhat.dto.response.SubjectResponseDTO;
import com.example.authnuzhat.exception.ConflictException;
import com.example.authnuzhat.exception.ResourceNotFoundException;
import com.example.authnuzhat.models.Subject;
import com.example.authnuzhat.repository.QuestionRepository;
import com.example.authnuzhat.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class SubjectService implements ISubjectService{
    private final SubjectRepository subjectRepository;
    private final QuestionRepository questionRepository;


    @Override
    public SubjectResponseDTO createSubject(SubjectRequestDTO subjectRequestDTO) {
        // Check if a Subject with the same name already exists
        if (subjectRepository.existsByName(subjectRequestDTO.getName())) {
            throw new IllegalArgumentException("Subject with name '" + subjectRequestDTO.getName() + "' already exists.");
        }

        // Map DTO to Entity
        Subject subject = new Subject();
        subject.setName(subjectRequestDTO.getName());

        // Set parent if provided
        if (subjectRequestDTO.getParentId() != null) {
            Subject parent = subjectRepository.findById(subjectRequestDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Subject not found with id: " + subjectRequestDTO.getParentId()));
            subject.setParent(parent);
        }

        // Save to database
        Subject savedSubject = subjectRepository.save(subject);

        // Map Entity to DTO
        return mapToResponseDTO(savedSubject);
    }

    @Override
    public List<SubjectResponseDTO> getAllSubjects() {
        // Fetch all Subjects from the database
        List<Subject> subjects = subjectRepository.findAll();

        // Map Entities to DTOs
        return subjects.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SubjectResponseDTO getSubjectById(Long id) {
        // Fetch Subject by ID or throw an exception if not found
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));

        // Map Entity to DTO
        return mapToResponseDTO(subject);
    }

    @Override
    public SubjectResponseDTO updateSubject(Long id, SubjectRequestDTO subjectRequestDTO) {
        // Fetch Subject by ID or throw an exception if not found
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));

        // Update fields
        subject.setName(subjectRequestDTO.getName());

        // Update parent if provided
        if (subjectRequestDTO.getParentId() != null) {
            Subject parent = subjectRepository.findById(subjectRequestDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Subject not found with id: " + subjectRequestDTO.getParentId()));
            subject.setParent(parent);
        } else {
            subject.setParent(null); // Remove parent if parentId is null
        }

        // Save updated entity
        Subject updatedSubject = subjectRepository.save(subject);

        // Map Entity to DTO
        return mapToResponseDTO(updatedSubject);
    }

    @Override
    public void deleteSubject(Long id) {
        // Check if Subject exists
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject not found with id: " + id);
        }

        // Check if there are dependent questions, if yes, then we will not delete that subject
        if (questionRepository.existsBySubjectId(id)) {
            throw new ConflictException("Cannot delete subject: It has associated questions.");
        }

        // Delete Subject
        subjectRepository.deleteById(id);

    }

    @Override
    public List<SubjectResponseDTO> getAllRootSubjects() {
        // Fetch all root subjects (subjects with no parent)
        List<Subject> rootSubjects = subjectRepository.findByParentIsNull();

        // Map Entities to DTOs
        return rootSubjects.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Helper method to map Entity to DTO
    private SubjectResponseDTO mapToResponseDTO(Subject subject) {
        SubjectResponseDTO dto = new SubjectResponseDTO();
        dto.setId(subject.getId());
        dto.setName(subject.getName());

        if (subject.getParent() != null) {
            dto.setParentId(subject.getParent().getId());
            dto.setParentName(subject.getParent().getName());
        }

        // Optional: keep IDs
        dto.setChildrenIds(
                subject.getChildren().stream()
                        .map(Subject::getId)
                        .collect(Collectors.toList())
        );

        // New: map to child DTOs
        dto.setChildren(
                subject.getChildren().stream()
                        .map(child -> new ChildSubjectDTO(child.getId(), child.getName()))
                        .collect(Collectors.toList())
        );

        return dto;
    }
}