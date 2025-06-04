package com.example.authnuzhat.services.difficultyLevel;

import com.example.authnuzhat.dto.request.DifficultyLevelRequestDTO;
import com.example.authnuzhat.dto.response.DifficultyLevelResponseDTO;

import java.util.List;

public interface IDifficultyLevelService {
    // Create a new DifficultyLevel
    DifficultyLevelResponseDTO createDifficultyLevel(DifficultyLevelRequestDTO difficultyLevelRequestDTO);

    // Get all DifficultyLevels
    List<DifficultyLevelResponseDTO> getAllDifficultyLevels();

    // Get a DifficultyLevel by ID
    DifficultyLevelResponseDTO getDifficultyLevelById(Long id);

    // Update a DifficultyLevel
    DifficultyLevelResponseDTO updateDifficultyLevel(Long id, DifficultyLevelRequestDTO difficultyLevelRequestDTO);

    // Delete a DifficultyLevel
    void deleteDifficultyLevel(Long id);
}