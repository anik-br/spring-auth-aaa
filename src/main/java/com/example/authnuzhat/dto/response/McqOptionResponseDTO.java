package com.example.authnuzhat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class McqOptionResponseDTO {
    private Long id;
    private String optionText;
    private boolean correct;
    private Integer sortOrder;
    private String imageUrl;
}