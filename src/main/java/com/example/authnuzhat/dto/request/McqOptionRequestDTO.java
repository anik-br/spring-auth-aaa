package com.example.authnuzhat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class McqOptionRequestDTO {

    @NotBlank(message = "option text is required")
    private String optionText;

    @NotBlank(message = "Answer status is required")
    private boolean correct;

    private Integer sortOrder;
    // Optional field to map image file by name
    private String imageKey;
    private String imageUrl;
}