package com.example.authnuzhat.controllers;

import com.example.authnuzhat.dto.request.TeacherRegistrationRequest;
import com.example.authnuzhat.payload.response.MessageResponse;
import com.example.authnuzhat.services.RegistrationService;
import com.example.authnuzhat.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
