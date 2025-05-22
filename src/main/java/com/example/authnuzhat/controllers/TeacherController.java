package com.example.authnuzhat.controllers;

import com.example.authnuzhat.dto.request.TeacherRegistrationRequest;
import com.example.authnuzhat.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/register")
    public ResponseEntity<?> registerTeacher(@RequestBody TeacherRegistrationRequest request) {
        return ResponseEntity.ok(teacherService.registerTeacher(request));
    }
}
