package com.example.authnuzhat.controllers;

import com.example.authnuzhat.dto.request.AdminRegistrationRequest;
import com.example.authnuzhat.dto.request.TeacherRegistrationRequest;
import com.example.authnuzhat.payload.response.MessageResponse;
import com.example.authnuzhat.services.RegistrationService;
import com.example.authnuzhat.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/signup/teacher")
    public ResponseEntity<?> registerTeacher(@Valid @RequestBody TeacherRegistrationRequest signupRequest) {
        try {
            registrationService.registerTeacher(signupRequest);
            return ResponseEntity.ok(new MessageResponse("Teacher registered successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/signup/admin")
    @PreAuthorize("hasAuthority('CREATE_ADMIN_BY_SUPER_ADMIN_ONLY')")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminRegistrationRequest signupRequest) {
        try {
            registrationService.registerAdmin(signupRequest);
            return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
