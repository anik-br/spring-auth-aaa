package com.example.authnuzhat.dto.request;

import com.example.authnuzhat.models.QuestionImportance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDTO {

    @NotBlank(message = "Question text is required")
    private String questionText;

    @NotNull(message = "Question type ID is required")
    private Long questionTypeId;
    private Long difficultyLevelId;
    private Long subjectId;
    private Long classLevelId;
    private QuestionImportance importance;

    private List<McqOptionRequestDTO> mcqOptions; // Optional: For MCQ questions
    private AnswerRequestDTO answer; // Optional: For non-MCQ questions
    private List<ImageRequestDTO> images; // Optional: Images associated with the question

    private Integer score = 1; // Default score is 1
    private String createdBy;
    private String modifiedBy;
    private boolean active = true; // Default is active
    private String explanation;
}