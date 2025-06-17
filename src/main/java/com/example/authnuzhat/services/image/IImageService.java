package com.example.authnuzhat.services.image;

import com.example.authnuzhat.dto.request.ImageRequestDTO;
import com.example.authnuzhat.dto.response.ImageResponseDTO;

import java.io.IOException;
import java.util.List;


public interface IImageService {

    ImageResponseDTO createImage(ImageRequestDTO imageRequestDTO) throws IOException;

    ImageResponseDTO getImageById(Long id);

    List<ImageResponseDTO> getAllImages();

    ImageResponseDTO updateImage(Long id, ImageRequestDTO imageRequestDTO) throws IOException;

    void deleteImage(Long id);

    List<ImageResponseDTO> getImagesByQuestionId(Long questionId); // New method to fetch images by question ID

    void deleteImageByUrl(String imageUrl);

    void deleteByQuestionId(Long questionId);
}