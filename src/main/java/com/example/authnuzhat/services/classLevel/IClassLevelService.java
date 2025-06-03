package com.example.authnuzhat.services.classLevel;

import com.example.authnuzhat.dto.request.ClassLevelRequestDTO;
import com.example.authnuzhat.dto.response.ClassLevelResponseDTO;

import java.util.List;

public interface IClassLevelService {
    // Create a new ClassLevel
    ClassLevelResponseDTO createClassLevel(ClassLevelRequestDTO classLevelRequestDTO);

    // Get all ClassLevels
    List<ClassLevelResponseDTO> getAllClassLevels();

    // Get a ClassLevel by ID
    ClassLevelResponseDTO getClassLevelById(Long id);

    // Update a ClassLevel
    ClassLevelResponseDTO updateClassLevel(Long id, ClassLevelRequestDTO classLevelRequestDTO);

    // Delete a ClassLevel
    void deleteClassLevel(Long id);
}