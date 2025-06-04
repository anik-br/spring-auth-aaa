package com.example.authnuzhat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTypeResponseDTO {
    private Long id;
    private String name;
    private String description;
}