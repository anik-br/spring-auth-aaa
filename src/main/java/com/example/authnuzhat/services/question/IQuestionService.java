package com.example.authnuzhat.services.question;

import com.example.authnuzhat.dto.request.QuestionRequestDTO;
import com.example.authnuzhat.dto.response.QuestionResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IQuestionService {
    // Create a new Question
    QuestionResponseDTO createQuestion(QuestionRequestDTO questionRequestDTO, MultipartFile[] images, MultipartFile[] optionImages) throws IOException;

    // Get all Questions
    List<QuestionResponseDTO> getAllQuestions();

    // Get a Question by ID
    QuestionResponseDTO getQuestionById(Long id);

    // Update a Question
    QuestionResponseDTO updateQuestion(Long id, QuestionRequestDTO questionRequestDTO, MultipartFile[] images, MultipartFile[] optionImages) throws IOException;

    // Delete a Question
    void deleteQuestion(Long id);

    // Get all Questions by Subject ID
    List<QuestionResponseDTO> getQuestionsBySubjectId(Long subjectId);

    // Get all Questions by Class Level ID
    List<QuestionResponseDTO> getQuestionsByClassLevelId(Long classLevelId);

    // Get all Questions by Difficulty Level ID
    List<QuestionResponseDTO> getQuestionsByDifficultyLevelId(Long difficultyLevelId);

    // Get all Questions by Question Type ID
    List<QuestionResponseDTO> getQuestionsByQuestionTypeId(Long questionTypeId);

    // Get all active Questions
    List<QuestionResponseDTO> getActiveQuestions();
}