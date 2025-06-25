package com.example.authnuzhat.dto.request;

import lombok.Data;

@Data
public class ExamPaperQuestionDTO {
    private Long questionId;
    private String questionText;     // ✅ Add this
    private String questionType;     // ✅ Add this (for MCQ preview)
    private Integer mark;
}