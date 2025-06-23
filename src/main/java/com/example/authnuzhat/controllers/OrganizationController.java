package com.example.authnuzhat.controllers;

import com.example.authnuzhat.dto.request.OrganizationDTO;
import com.example.authnuzhat.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/all")
    // @PreAuthorize("hasAuthority('READ_USER')")
    public ResponseEntity<List<OrganizationDTO>> getAll() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SETUP_PERMISSION')")
    public ResponseEntity<OrganizationDTO> create(@RequestBody OrganizationDTO dto) {
        return ResponseEntity.ok(organizationService.createOrganization(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SETUP_PERMISSION')") // Assuming an UPDATE_USER authority
    public ResponseEntity<OrganizationDTO> update(@PathVariable Long id, @RequestBody OrganizationDTO dto) {
        return ResponseEntity.ok(organizationService.updateOrganization(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SETUP_PERMISSION')") // Assuming a DELETE_USER authority
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
    }
}