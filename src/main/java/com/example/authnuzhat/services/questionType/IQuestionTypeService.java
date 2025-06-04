package com.example.authnuzhat.services.questionType;

import com.example.authnuzhat.dto.request.QuestionTypeRequestDTO;
import com.example.authnuzhat.dto.response.QuestionTypeResponseDTO;

import java.util.List;

public interface IQuestionTypeService {
    // Create a new QuestionType
    QuestionTypeResponseDTO createQuestionType(QuestionTypeRequestDTO questionTypeRequestDTO);

    // Get all questions type
    List<QuestionTypeResponseDTO> getAllQuestionTypes();

    // Get a question type by id
    QuestionTypeResponseDTO getQuestionTypeById(Long id);

    //Update a question type
    QuestionTypeResponseDTO updateQuestionType(Long id, QuestionTypeRequestDTO questionTypeRequestDTO);

    // Delete a question type
    void deleteQuestionType(Long id);

}