package com.example.authnuzhat.services.image;


import com.example.authnuzhat.dto.request.ImageRequestDTO;
import com.example.authnuzhat.dto.response.ImageResponseDTO;
import com.example.authnuzhat.exception.ResourceNotFoundException;
import com.example.authnuzhat.models.Image;
import com.example.authnuzhat.models.Question;
import com.example.authnuzhat.repository.ImageRepository;
import com.example.authnuzhat.repository.QuestionRepository;
import com.example.authnuzhat.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {


    private final ImageRepository imageRepository;
    private final QuestionRepository questionRepository;
    private final FileStorageService fileStorageService;

    @Override
    public ImageResponseDTO createImage(ImageRequestDTO imageRequestDTO) throws IOException {
        // Fetch the associated question
        Question question = questionRepository.findById(imageRequestDTO.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + imageRequestDTO.getQuestionId()));

        // Save the uploaded file and get the file path
        MultipartFile file = imageRequestDTO.getFile();
        String filePath = fileStorageService.saveFile(file);

        // Create and save the image entity
        Image image = new Image();
        image.setUrl(filePath); // Store the file path or URL
        image.setDescription(imageRequestDTO.getDescription());
        image.setImageOrder(imageRequestDTO.getImageOrder());
        image.setQuestion(question);

        Image savedImage = imageRepository.save(image);

        // Map the entity to DTO
        return mapToImageResponseDTO(savedImage);
    }

    @Override
    public ImageResponseDTO getImageById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        return mapToImageResponseDTO(image);
    }

    @Override
    public List<ImageResponseDTO> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream()
                .map(this::mapToImageResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ImageResponseDTO updateImage(Long id, ImageRequestDTO imageRequestDTO) throws IOException {
        // Find the existing image by ID
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));

        // Update description if provided
        if (imageRequestDTO.getDescription() != null) {
            image.setDescription(imageRequestDTO.getDescription());
        }

        // Update image order if provided
        if (imageRequestDTO.getImageOrder() != null) {
            image.setImageOrder(imageRequestDTO.getImageOrder());
        }

        // Handle file upload (if a new file is provided)
        if (imageRequestDTO.getFile() != null) {
            // Save the new file and update the URL
            String filePath = fileStorageService.saveFile(imageRequestDTO.getFile());
            image.setUrl(filePath); // Update the URL to the new file path
        } else if (imageRequestDTO.getUrl() != null) {
            // If no file is provided but a URL is, update the URL directly
            image.setUrl(imageRequestDTO.getUrl());
        }

        // Save the updated image
        Image updatedImage = imageRepository.save(image);

        // Map the updated image to DTO and return
        return mapToImageResponseDTO(updatedImage);
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    public List<ImageResponseDTO> getImagesByQuestionId(Long questionId) {
        // Fetch all images associated with the given question ID
        List<Image> images = imageRepository.findByQuestionId(questionId);

        // Map each Image entity to ImageResponseDTO
        return images.stream()
                .map(this::mapToImageResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByQuestionId(Long questionId) {
        // Fetch all images associated with the question ID
        List<Image> images = imageRepository.findByQuestionId(questionId);

        // Delete each image file from the storage
        for (Image image : images) {
            fileStorageService.deleteFile(image.getUrl()); // Ensure this method is implemented
        }

        // Delete all image records from the database
        imageRepository.deleteByQuestionId(questionId);
    }

    @Override
    public void deleteImageByUrl(String url) {
        // First delete the file from the storage
        if (url != null && !url.isEmpty()) {
            fileStorageService.deleteFile(url); // Make sure this method handles exceptions internally
        }

        // Then delete the record from the database
        imageRepository.deleteByUrl(url);
    }


    private ImageResponseDTO mapToImageResponseDTO(Image image) {
        ImageResponseDTO imageResponseDTO = new ImageResponseDTO();
        imageResponseDTO.setId(image.getId());
        imageResponseDTO.setUrl(image.getUrl());
        imageResponseDTO.setDescription(image.getDescription());
        imageResponseDTO.setImageOrder(image.getImageOrder());
        imageResponseDTO.setQuestionId(image.getQuestion().getId());
        return imageResponseDTO;
    }
}