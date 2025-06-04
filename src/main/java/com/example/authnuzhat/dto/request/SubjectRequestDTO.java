package com.example.authnuzhat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SubjectRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;
    private Long parentId; // ID of the parent subject (optional)
}