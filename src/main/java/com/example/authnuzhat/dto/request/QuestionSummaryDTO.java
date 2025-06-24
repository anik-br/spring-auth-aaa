package com.example.authnuzhat.dto.request;

import lombok.Data;

@Data
public class QuestionSummaryDTO {
    private Long id;
    private String questionText;
    private String questionType;
    private String subject;
}