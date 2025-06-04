package com.example.authnuzhat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDTO {
    private Long id;
    private List<Long> correctOptionIds; // IDs of correct MCQ options
    private Boolean correctBoolean;
    private String correctText;
    private Double numericalValue;
    private Double numericalTolerance;
    private Double score;
    private Boolean allowPartialScoring;
    private BigDecimal negativeScore;
}