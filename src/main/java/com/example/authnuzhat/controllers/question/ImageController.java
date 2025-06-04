package com.example.authnuzhat.controllers.question;

import com.example.authnuzhat.dto.request.ImageRequestDTO;
import com.example.authnuzhat.dto.response.ImageResponseDTO;
import com.example.authnuzhat.services.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageController {

    private final IImageService imageService;

    // Create a new image with file upload
    @PostMapping
    public ResponseEntity<ImageResponseDTO> createImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            @RequestParam("imageOrder") Integer imageOrder,
            @RequestParam("questionId") Long questionId) throws IOException {

        ImageRequestDTO imageRequestDTO = new ImageRequestDTO();
        imageRequestDTO.setFile(file);
        imageRequestDTO.setDescription(description);
        imageRequestDTO.setImageOrder(imageOrder);
        imageRequestDTO.setQuestionId(questionId);

        ImageResponseDTO imageResponseDTO = imageService.createImage(imageRequestDTO);
        return new ResponseEntity<>(imageResponseDTO, HttpStatus.CREATED);
    }

    // Get an image by ID
    @GetMapping("/{id}")
    public ResponseEntity<ImageResponseDTO> getImageById(@PathVariable Long id) {
        ImageResponseDTO imageResponseDTO = imageService.getImageById(id);
        return new ResponseEntity<>(imageResponseDTO, HttpStatus.OK);
    }

    // Get all images
    @GetMapping
    public ResponseEntity<List<ImageResponseDTO>> getAllImages() {
        List<ImageResponseDTO> imageResponseDTOs = imageService.getAllImages();
        return new ResponseEntity<>(imageResponseDTOs, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImageResponseDTO> updateImage(
            @PathVariable Long id,
            @ModelAttribute ImageRequestDTO imageRequestDTO) throws IOException {

        // Call the service to update the image
        ImageResponseDTO updatedImage = imageService.updateImage(id, imageRequestDTO);

        // Return the updated image with HTTP 200 OK
        return new ResponseEntity<>(updatedImage, HttpStatus.OK);
    }

    // Delete an image by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all images by question ID
    @GetMapping("/by-question/{questionId}")
    public ResponseEntity<List<ImageResponseDTO>> getImagesByQuestionId(@PathVariable Long questionId) {
        List<ImageResponseDTO> imageResponseDTOs = imageService.getImagesByQuestionId(questionId);
        return new ResponseEntity<>(imageResponseDTOs, HttpStatus.OK);
    }
}