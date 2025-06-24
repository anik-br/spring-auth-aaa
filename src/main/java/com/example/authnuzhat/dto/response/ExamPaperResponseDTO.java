package com.example.authnuzhat.dto.response;

import com.example.authnuzhat.dto.request.ExamPaperQuestionDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamPaperResponseDTO {
    private Long id;
    private String title;
    private String createdBy;
    private String duration;
    private LocalDateTime createdAt;
    private List<ExamPaperQuestionDTO> questions; // questionId + mark
}