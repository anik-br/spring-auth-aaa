package com.example.authnuzhat.controllers.question;


import com.example.authnuzhat.dto.request.QuestionRequestDTO;
import com.example.authnuzhat.dto.request.QuestionSummaryDTO;
import com.example.authnuzhat.dto.response.QuestionResponseDTO;
import com.example.authnuzhat.exception.ResourceNotFoundException;
import com.example.authnuzhat.services.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("${api.prefix}/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    // Create a new Question
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('CREATE_QUESTION')")
    public ResponseEntity<QuestionResponseDTO> createQuestion(
            @RequestPart("question") @Valid QuestionRequestDTO questionRequestDTO,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "optionImages", required = false) MultipartFile[] optionImages) {

        try {
            QuestionResponseDTO responseDTO = questionService.createQuestion(questionRequestDTO, images, optionImages);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    // Get all Questions
    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN_ACCESS')")
    public ResponseEntity<List<QuestionResponseDTO>> getAllQuestions() {
        List<QuestionResponseDTO> responseDTOs = questionService.getAllQuestions();
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get a Question by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CREATE_QUESTION')")
    public ResponseEntity<QuestionResponseDTO> getQuestionById(@PathVariable Long id) {
        QuestionResponseDTO responseDTO = questionService.getQuestionById(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Update a Question
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('CREATE_QUESTION')")
    public ResponseEntity<QuestionResponseDTO> updateQuestion(
            @PathVariable Long id,
            @RequestPart("question") @Valid QuestionRequestDTO questionRequestDTO,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "optionImages", required = false) MultipartFile[] optionImages) {

        try {
            QuestionResponseDTO responseDTO = questionService.updateQuestion(id, questionRequestDTO, images, optionImages);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Delete a Question
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CREATE_QUESTION')")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all Questions by Subject ID
    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAuthority('CREATE_QUESTION')")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsBySubjectId(@PathVariable Long subjectId) {
        List<QuestionResponseDTO> responseDTOs = questionService.getQuestionsBySubjectId(subjectId);
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get all Questions by Class Level ID
    @GetMapping("/class-level/{classLevelId}")
    @PreAuthorize("hasAuthority('CREATE_QUESTION')")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByClassLevelId(@PathVariable Long classLevelId) {
        List<QuestionResponseDTO> responseDTOs = questionService.getQuestionsByClassLevelId(classLevelId);
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get all Questions by Difficulty Level ID
    @GetMapping("/difficulty-level/{difficultyLevelId}")
    @PreAuthorize("hasAuthority('CREATE_QUESTION')")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByDifficultyLevelId(@PathVariable Long difficultyLevelId) {
        List<QuestionResponseDTO> responseDTOs = questionService.getQuestionsByDifficultyLevelId(difficultyLevelId);
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get all Questions by Question Type ID
    @GetMapping("/question-type/{questionTypeId}")
    @PreAuthorize("hasAuthority('CREATE_QUESTION')")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByQuestionTypeId(@PathVariable Long questionTypeId) {
        List<QuestionResponseDTO> responseDTOs = questionService.getQuestionsByQuestionTypeId(questionTypeId);
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get all active Questions
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('CREATE_QUESTION')")
    public ResponseEntity<List<QuestionResponseDTO>> getActiveQuestions() {
        List<QuestionResponseDTO> responseDTOs = questionService.getActiveQuestions();
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    @GetMapping("/for-exam-paper")
    @PreAuthorize("hasAuthority('CREATE_EXAM_PAPER')")
    public ResponseEntity<List<QuestionSummaryDTO>> getAllForExamPaper() {
        return ResponseEntity.ok(questionService.getAllForExamPaper());
    }

}