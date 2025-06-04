package com.example.authnuzhat.controllers.question;

import com.example.authnuzhat.dto.request.SubjectRequestDTO;
import com.example.authnuzhat.dto.response.SubjectResponseDTO;
import com.example.authnuzhat.services.subject.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/subjects")
@RequiredArgsConstructor

public class SubjectController {
    private final SubjectService subjectService;

    // create a new Subject
    @PostMapping("/subject")
    public ResponseEntity<SubjectResponseDTO> createSubject(@Valid @RequestBody SubjectRequestDTO subjectRequestDTO) {
        SubjectResponseDTO responseDTO = subjectService.createSubject(subjectRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Get all Subjects
    @GetMapping("/all")
    public ResponseEntity<List<SubjectResponseDTO>> getAllSubjects() {
        List<SubjectResponseDTO> responseDTOs = subjectService.getAllSubjects();
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get a Subject by ID
    @GetMapping("/subject/{id}")
    public ResponseEntity<SubjectResponseDTO> getSubjectById(@PathVariable Long id) {
        SubjectResponseDTO responseDTO = subjectService.getSubjectById(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Update a Subject
    @PutMapping("/update/{id}")
    public ResponseEntity<SubjectResponseDTO> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody SubjectRequestDTO subjectRequestDTO) {
        SubjectResponseDTO responseDTO = subjectService.updateSubject(id, subjectRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Delete a Subject
    @DeleteMapping("/subject/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // Get all root subjects (subjects with no parent)
    @GetMapping("/roots")
    public ResponseEntity<List<SubjectResponseDTO>> getAllRootSubjects() {
        List<SubjectResponseDTO> responseDTOs = subjectService.getAllRootSubjects();
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

}