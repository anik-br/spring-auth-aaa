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

    @GetMapping(("/all"))
   // @PreAuthorize("hasAuthority('READ_USER')")
    public ResponseEntity<List<OrganizationDTO>> getAll() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @PostMapping
   @PreAuthorize("hasAuthority('CREATE_USER')")
    public ResponseEntity<OrganizationDTO> create(@RequestBody OrganizationDTO dto) {
        return ResponseEntity.ok(organizationService.createOrganization(dto));
    }
}