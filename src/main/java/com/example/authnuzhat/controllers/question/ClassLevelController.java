package com.example.authnuzhat.controllers.question;

import com.example.authnuzhat.dto.request.ClassLevelRequestDTO;
import com.example.authnuzhat.dto.response.ClassLevelResponseDTO;
import com.example.authnuzhat.services.classLevel.ClassLevelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/class-levels")
@RequiredArgsConstructor
public class ClassLevelController {
    private final ClassLevelService classLevelService;

    // Create a new ClassLevel
    @PostMapping("/class-level")
    public ResponseEntity<ClassLevelResponseDTO> createClassLevel(@Valid @RequestBody ClassLevelRequestDTO classLevelRequestDTO) {
        ClassLevelResponseDTO responseDTO = classLevelService.createClassLevel(classLevelRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Get all ClassLevels
    @GetMapping("/all")
    public ResponseEntity<List<ClassLevelResponseDTO>> getAllClassLevels() {
        List<ClassLevelResponseDTO> responseDTOs = classLevelService.getAllClassLevels();
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get a ClassLevel by ID
    @GetMapping("/class-level/{id}")
    public ResponseEntity<ClassLevelResponseDTO> getClassLevelById(@PathVariable Long id) {
        ClassLevelResponseDTO responseDTO = classLevelService.getClassLevelById(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Update a ClassLevel
    @PutMapping("/class-level/{id}")
    public ResponseEntity<ClassLevelResponseDTO> updateClassLevel(
            @PathVariable Long id,
            @Valid @RequestBody ClassLevelRequestDTO classLevelRequestDTO) {
        ClassLevelResponseDTO responseDTO = classLevelService.updateClassLevel(id, classLevelRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Delete a ClassLevel
    @DeleteMapping("/class-level/{id}")
    public ResponseEntity<Void> deleteClassLevel(@PathVariable Long id) {
        classLevelService.deleteClassLevel(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}