package com.example.authnuzhat.controllers.question;

import com.example.authnuzhat.dto.request.DifficultyLevelRequestDTO;
import com.example.authnuzhat.dto.response.DifficultyLevelResponseDTO;
import com.example.authnuzhat.services.difficultyLevel.DifficultyLevelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/difficulty-levels")
@RequiredArgsConstructor

public class DifficultyLevelController {

    private final DifficultyLevelService difficultyLevelService;

    // Create a new DifficultyLevel
    @PostMapping("/difficulty-level")
    public ResponseEntity<DifficultyLevelResponseDTO> createDifficultyLevel(@Valid @RequestBody DifficultyLevelRequestDTO difficultyLevelRequestDTO) {
        DifficultyLevelResponseDTO responseDTO = difficultyLevelService.createDifficultyLevel(difficultyLevelRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Get all DifficultyLevels
    @GetMapping("/all")
    public ResponseEntity<List<DifficultyLevelResponseDTO>> getAllDifficultyLevels() {
        List<DifficultyLevelResponseDTO> responseDTOs = difficultyLevelService.getAllDifficultyLevels();
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get a DifficultyLevel by ID
    @GetMapping("/{id}")
    public ResponseEntity<DifficultyLevelResponseDTO> getDifficultyLevelById(@PathVariable Long id) {
        DifficultyLevelResponseDTO responseDTO = difficultyLevelService.getDifficultyLevelById(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Update a DifficultyLevel
    @PutMapping("/{id}")
    public ResponseEntity<DifficultyLevelResponseDTO> updateDifficultyLevel(
            @PathVariable Long id,
            @Valid @RequestBody DifficultyLevelRequestDTO difficultyLevelRequestDTO) {
        DifficultyLevelResponseDTO responseDTO = difficultyLevelService.updateDifficultyLevel(id, difficultyLevelRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Delete a DifficultyLevel
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDifficultyLevel(@PathVariable Long id) {
        difficultyLevelService.deleteDifficultyLevel(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}