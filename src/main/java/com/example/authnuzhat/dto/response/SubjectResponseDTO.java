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
    private Long parentId; // ID of the parent subject
    private List<Long> childrenIds; // IDs of child subjects
}