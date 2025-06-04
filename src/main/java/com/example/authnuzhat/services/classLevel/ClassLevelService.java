package com.example.authnuzhat.services.classLevel;

import com.example.authnuzhat.dto.request.ClassLevelRequestDTO;
import com.example.authnuzhat.dto.response.ClassLevelResponseDTO;
import com.example.authnuzhat.exception.ResourceNotFoundException;
import com.example.authnuzhat.models.ClassLevel;
import com.example.authnuzhat.repository.ClassLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassLevelService implements IClassLevelService {
    private final ClassLevelRepository classLevelRepository;

    @Override
    public ClassLevelResponseDTO createClassLevel(ClassLevelRequestDTO classLevelRequestDTO) {
        // Check if a ClassLevel with the same name already exists
        if (classLevelRepository.existsByName(classLevelRequestDTO.getName())) {
            throw new IllegalArgumentException("ClassLevel with name '" + classLevelRequestDTO.getName() + "' already exists.");
        }

        // Map DTO to Entity
        ClassLevel classLevel = new ClassLevel();
        classLevel.setName(classLevelRequestDTO.getName());
        classLevel.setCurriculum(classLevelRequestDTO.getCurriculum());
        classLevel.setEducationLevel(classLevelRequestDTO.getEducationLevel());

        // Save to database
        ClassLevel savedClassLevel = classLevelRepository.save(classLevel);

        // Map Entity to DTO
        return mapToResponseDTO(savedClassLevel);
    }

    @Override
    public List<ClassLevelResponseDTO> getAllClassLevels() {
        // Fetch all ClassLevels from the database
        List<ClassLevel> classLevels = classLevelRepository.findAll();

        // Map Entities to DTOs
        return classLevels.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClassLevelResponseDTO getClassLevelById(Long id) {
        // Fetch ClassLevel by ID or throw an exception if not found
        ClassLevel classLevel = classLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassLevel not found with id: " + id));

        // Map Entity to DTO
        return mapToResponseDTO(classLevel);
    }

    @Override
    public ClassLevelResponseDTO updateClassLevel(Long id, ClassLevelRequestDTO classLevelRequestDTO) {
        // Fetch ClassLevel by ID or throw an exception if not found
        ClassLevel classLevel = classLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassLevel not found with id: " + id));

        // Update fields
        classLevel.setName(classLevelRequestDTO.getName());
        classLevel.setCurriculum(classLevelRequestDTO.getCurriculum());
        classLevel.setEducationLevel(classLevelRequestDTO.getEducationLevel());

        // Save updated entity
        ClassLevel updatedClassLevel = classLevelRepository.save(classLevel);

        // Map Entity to DTO
        return mapToResponseDTO(updatedClassLevel);
    }

    @Override
    public void deleteClassLevel(Long id) {
        // Check if ClassLevel exists
        if (!classLevelRepository.existsById(id)) {
            throw new ResourceNotFoundException("ClassLevel not found with id: " + id);
        }

        // Delete ClassLevel
        classLevelRepository.deleteById(id);
    }

    // Helper method to map Entity to DTO
    private ClassLevelResponseDTO mapToResponseDTO(ClassLevel classLevel) {
        return new ClassLevelResponseDTO(
                classLevel.getId(),
                classLevel.getName(),
                classLevel.getCurriculum(),
                classLevel.getEducationLevel()
        );
    }
}