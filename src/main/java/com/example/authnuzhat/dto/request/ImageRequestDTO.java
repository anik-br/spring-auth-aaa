package com.example.authnuzhat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequestDTO {

    private MultipartFile file; // The image file to upload
    private String url;        // For direct URL updates
    private String description; // Description of the image
    private Integer imageOrder; // Order of the image
    private Long questionId;   // ID of the associated question
}