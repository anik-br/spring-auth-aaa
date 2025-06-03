package com.example.authnuzhat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassLevelResponseDTO {

    private Long id;
    private String name;
    private String curriculum;
    private String educationLevel;
}