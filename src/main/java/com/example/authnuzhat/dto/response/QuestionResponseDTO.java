package com.example.authnuzhat.dto.response;

import com.example.authnuzhat.models.QuestionImportance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDTO {

    private Long id;
    private String questionText;
    private Long questionTypeId;
    private Long difficultyLevelId;
    private Long subjectId;
    private Long classLevelId;
    private QuestionImportance importance;
    private List<McqOptionResponseDTO> mcqOptions; // For MCQ questions
    private AnswerResponseDTO answer; // For non-MCQ questions
    private List<ImageResponseDTO> images; // Images associated with the question
    private Integer score;
    private String createdBy;
    private String modifiedBy;
    private boolean active;
    private String explanation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}