package com.example.authnuzhat.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}") // Define the upload directory in application.properties
    private String uploadDir;

    /**
     * Saves a file to the specified upload directory and returns the file path.
     *
     * @param file The file to save.
     * @return The file path where the file is stored.
     * @throws IOException If the file cannot be saved.
     */
    public String saveFile(MultipartFile file) throws IOException {
        // Ensure the upload directory exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate a unique file name to avoid conflicts
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Save the file to the specified path
        Files.copy(file.getInputStream(), filePath);

        // Return the file path or URL
        return filePath.toString();
    }

    /**
     * Deletes a file from the storage given its path.
     *
     * @param filePath The path of the file to delete.
     */
    public void deleteFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + filePath, e);
        }
    }
}