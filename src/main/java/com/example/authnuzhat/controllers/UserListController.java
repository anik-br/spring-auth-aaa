package com.example.authnuzhat.controllers;

import com.example.authnuzhat.repository.AdminRepository;
import com.example.authnuzhat.repository.TeacherRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserListController {
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;

    public UserListController(TeacherRepository teacherRepository, AdminRepository adminRepository) {
        this.teacherRepository = teacherRepository;
        this.adminRepository = adminRepository;
    }

    // Get list of all teachers (accessible to users with ADMIN_ACCESS)
    @GetMapping("/teachers")
    @PreAuthorize("hasAuthority('ADMIN_ACCESS')")
    public ResponseEntity<?> getAllTeachers() {
        return ResponseEntity.ok(teacherRepository.findAll());
    }

    // Get list of all admins (accessible to users with SUPER_ADMIN_ACCESS)
    @GetMapping("/admins")
    @PreAuthorize("hasAuthority('SUPER_ADMIN_ACCESS')")
    public ResponseEntity<?> getAllAdmins() {
        return ResponseEntity.ok(adminRepository.findAll());
    }
}
