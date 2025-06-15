package com.example.authnuzhat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponseDTO {
    private Long id;
    private String name;
    private Long parentId;
    private String parentName;

    private List<Long> childrenIds;                 // Optional: keep if needed
    private List<ChildSubjectDTO> children;         // ✅ New field for ID + name
}