package com.example.authnuzhat.dto.request;


import lombok.Data;

import java.util.List;
@Data
public class ExamPaperRequestDTO {
    private String title;
    private String createdBy;
    private String duration;
    private List<ExamPaperQuestionDTO> questions; // questionId + mark
}
