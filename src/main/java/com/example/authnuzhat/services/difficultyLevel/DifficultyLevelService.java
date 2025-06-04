package com.example.authnuzhat.services.difficultyLevel;


import com.example.authnuzhat.dto.request.DifficultyLevelRequestDTO;
import com.example.authnuzhat.dto.response.DifficultyLevelResponseDTO;
import com.example.authnuzhat.exception.ResourceNotFoundException;
import com.example.authnuzhat.models.DifficultyLevel;
import com.example.authnuzhat.repository.DifficultyLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DifficultyLevelService implements IDifficultyLevelService{
    private final DifficultyLevelRepository difficultyLevelRepository;

    @Override
    public DifficultyLevelResponseDTO createDifficultyLevel(DifficultyLevelRequestDTO difficultyLevelRequestDTO) {
        // Check if a DifficultyLevel with the same level already exists
        if (difficultyLevelRepository.existsByLevel(difficultyLevelRequestDTO.getLevel())) {
            throw new IllegalArgumentException("DifficultyLevel with level '" + difficultyLevelRequestDTO.getLevel() + "' already exists.");
        }

        // Map DTO to Entity
        DifficultyLevel difficultyLevel = new DifficultyLevel();
        difficultyLevel.setLevel(difficultyLevelRequestDTO.getLevel());

        // Save to database
        DifficultyLevel savedDifficultyLevel = difficultyLevelRepository.save(difficultyLevel);

        // Map Entity to DTO
        return mapToResponseDTO(savedDifficultyLevel);
    }

    @Override
    public List<DifficultyLevelResponseDTO> getAllDifficultyLevels() {
        // Fetch all DifficultyLevels from the database
        List<DifficultyLevel> difficultyLevels = difficultyLevelRepository.findAll();

        // Map Entities to DTOs
        return difficultyLevels.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

    }

    @Override
    public DifficultyLevelResponseDTO getDifficultyLevelById(Long id) {

        // Fetch DifficultyLevel by ID or throw an exception if not found
        DifficultyLevel difficultyLevel = difficultyLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DifficultyLevel not found with id: " + id));

        // Map Entity to DTO
        return mapToResponseDTO(difficultyLevel);
    }

    @Override
    public DifficultyLevelResponseDTO updateDifficultyLevel(Long id, DifficultyLevelRequestDTO difficultyLevelRequestDTO) {
        // Fetch DifficultyLevel by ID or throw an exception if not found
        DifficultyLevel difficultyLevel = difficultyLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DifficultyLevel not found with id: " + id));

        // Update fields
        difficultyLevel.setLevel(difficultyLevelRequestDTO.getLevel());

        // Save updated entity
        DifficultyLevel updatedDifficultyLevel = difficultyLevelRepository.save(difficultyLevel);

        // Map Entity to DTO
        return mapToResponseDTO(updatedDifficultyLevel);
    }

    @Override
    public void deleteDifficultyLevel(Long id) {
        // Check if DifficultyLevel exists
        if (!difficultyLevelRepository.existsById(id)) {
            throw new ResourceNotFoundException("DifficultyLevel not found with id: " + id);
        }

        // Delete DifficultyLevel
        difficultyLevelRepository.deleteById(id);

    }

    // Helper method to map Entity to DTO
    private DifficultyLevelResponseDTO mapToResponseDTO(DifficultyLevel difficultyLevel) {
        return new DifficultyLevelResponseDTO(
                difficultyLevel.getId(),
                difficultyLevel.getLevel()
        );
    }
}