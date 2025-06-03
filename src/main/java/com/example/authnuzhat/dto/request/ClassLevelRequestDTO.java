package com.example.authnuzhat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassLevelRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String curriculum; // Optional: Cambridge, Oxford, etc.
    private String educationLevel; // Optional: Primary, Secondary, etc.
}