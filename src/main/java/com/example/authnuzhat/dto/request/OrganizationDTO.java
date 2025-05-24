package com.example.authnuzhat.dto.request;

import com.example.authnuzhat.models.OrganizationType;
import lombok.Data;

@Data
public class OrganizationDTO {
    private Long id;
    private String name;
    private OrganizationType type;
}