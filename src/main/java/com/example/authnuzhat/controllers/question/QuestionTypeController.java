package com.example.authnuzhat.controllers.question;

import com.example.authnuzhat.dto.request.QuestionTypeRequestDTO;
import com.example.authnuzhat.dto.response.QuestionTypeResponseDTO;
import com.example.authnuzhat.services.questionType.QuestionTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/question-types")
@RequiredArgsConstructor

public class QuestionTypeController {
    private final QuestionTypeService questionTypeService;

    // Create a new question type
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SETUP_PERMISSION')")
    public ResponseEntity<QuestionTypeResponseDTO> createQuestionType(@Valid @RequestBody QuestionTypeRequestDTO questionTypeRequestDTO){
        QuestionTypeResponseDTO responseDTO= questionTypeService.createQuestionType(questionTypeRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // get all questions
    @GetMapping("/ui/all")
    //@PreAuthorize("hasAuthority('SETUP_PERMISSION')")
    public ResponseEntity<List<QuestionTypeResponseDTO>> getAllQuestionTypes() {
        List<QuestionTypeResponseDTO> responseDTOs = questionTypeService.getAllQuestionTypes();
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get a QuestionType by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SETUP_PERMISSION')")
    public ResponseEntity<QuestionTypeResponseDTO> getQuestionTypeById(@PathVariable Long id) {
        QuestionTypeResponseDTO responseDTO = questionTypeService.getQuestionTypeById(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Update a QuestionType
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('SETUP_PERMISSION')")
    public ResponseEntity<QuestionTypeResponseDTO> updateQuestionType(
            @PathVariable Long id,
            @Valid @RequestBody QuestionTypeRequestDTO questionTypeRequestDTO) {
        QuestionTypeResponseDTO responseDTO = questionTypeService.updateQuestionType(id, questionTypeRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Delete a QuestionType
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SETUP_PERMISSION')")
    public ResponseEntity<Void> deleteQuestionType(@PathVariable Long id) {
        questionTypeService.deleteQuestionType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}