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
    private Long parentId;              // existing
    private String parentName;          // ✅ newly added
    private List<Long> childrenIds;     // existing
}