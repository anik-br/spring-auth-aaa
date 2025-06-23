package com.example.authnuzhat.services;

import com.example.authnuzhat.dto.request.OrganizationDTO;
import com.example.authnuzhat.models.Organization;
import com.example.authnuzhat.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public List<OrganizationDTO> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public OrganizationDTO createOrganization(OrganizationDTO dto) {
        Organization org = new Organization();
        org.setName(dto.getName());
        org.setType(dto.getType());

        Organization saved = organizationRepository.save(org);
        return mapToDTO(saved);
    }

    public OrganizationDTO updateOrganization(Long id, OrganizationDTO dto) {
        return organizationRepository.findById(id)
                .map(org -> {
                    org.setName(dto.getName());
                    org.setType(dto.getType());
                    Organization updated = organizationRepository.save(org);
                    return mapToDTO(updated);
                })
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
    }

    public void deleteOrganization(Long id) {
        organizationRepository.deleteById(id);
    }

    private OrganizationDTO mapToDTO(Organization org) {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setId(org.getId());
        dto.setName(org.getName());
        dto.setType(org.getType());
        return dto;
    }
}