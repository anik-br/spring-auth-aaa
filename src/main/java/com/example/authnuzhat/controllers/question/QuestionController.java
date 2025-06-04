package com.example.authnuzhat.controllers.question;


import com.example.authnuzhat.dto.request.QuestionRequestDTO;
import com.example.authnuzhat.dto.response.QuestionResponseDTO;
import com.example.authnuzhat.services.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    // Create a new Question
    @PostMapping
    public ResponseEntity<QuestionResponseDTO> createQuestion(@Valid @RequestBody QuestionRequestDTO questionRequestDTO) {
        QuestionResponseDTO responseDTO = questionService.createQuestion(questionRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Get all Questions
    @GetMapping
    public ResponseEntity<List<QuestionResponseDTO>> getAllQuestions() {
        List<QuestionResponseDTO> responseDTOs = questionService.getAllQuestions();
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get a Question by ID
    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> getQuestionById(@PathVariable Long id) {
        QuestionResponseDTO responseDTO = questionService.getQuestionById(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Update a Question
    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionRequestDTO questionRequestDTO) {
        QuestionResponseDTO responseDTO = questionService.updateQuestion(id, questionRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Delete a Question
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all Questions by Subject ID
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsBySubjectId(@PathVariable Long subjectId) {
        List<QuestionResponseDTO> responseDTOs = questionService.getQuestionsBySubjectId(subjectId);
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get all Questions by Class Level ID
    @GetMapping("/class-level/{classLevelId}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByClassLevelId(@PathVariable Long classLevelId) {
        List<QuestionResponseDTO> responseDTOs = questionService.getQuestionsByClassLevelId(classLevelId);
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get all Questions by Difficulty Level ID
    @GetMapping("/difficulty-level/{difficultyLevelId}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByDifficultyLevelId(@PathVariable Long difficultyLevelId) {
        List<QuestionResponseDTO> responseDTOs = questionService.getQuestionsByDifficultyLevelId(difficultyLevelId);
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get all Questions by Question Type ID
    @GetMapping("/question-type/{questionTypeId}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByQuestionTypeId(@PathVariable Long questionTypeId) {
        List<QuestionResponseDTO> responseDTOs = questionService.getQuestionsByQuestionTypeId(questionTypeId);
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get all active Questions
    @GetMapping("/active")
    public ResponseEntity<List<QuestionResponseDTO>> getActiveQuestions() {
        List<QuestionResponseDTO> responseDTOs = questionService.getActiveQuestions();
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }
}