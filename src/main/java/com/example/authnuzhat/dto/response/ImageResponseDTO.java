package com.example.authnuzhat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponseDTO {

    private Long id;
    private String url;
    private String description;
    private Integer imageOrder;
    private Long questionId; // ID of the associated question
}