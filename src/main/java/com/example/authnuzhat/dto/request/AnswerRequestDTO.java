package com.example.authnuzhat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AnswerRequestDTO {
    private Set<Long> correctOptionIds; // IDs of correct MCQ options
    private Boolean correctBoolean; // True/False answer
    private String correctText; // Text-based answer
    private Double numericalValue; // Numerical answer
    private Double numericalTolerance; // Tolerance for numerical answers
    private Double score; // Score for this answer
    private Boolean allowPartialScoring;
    private BigDecimal negativeScore;
}