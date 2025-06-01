package com.example.authnuzhat.services;

import com.example.authnuzhat.dto.request.AdminRegistrationRequest;
import com.example.authnuzhat.dto.request.TeacherRegistrationRequest;
import com.example.authnuzhat.models.Admin;
import com.example.authnuzhat.models.Organization;
import com.example.authnuzhat.models.Teacher;
import com.example.authnuzhat.models.User;
import com.example.authnuzhat.repository.AdminRepository;
import com.example.authnuzhat.repository.OrganizationRepository;
import com.example.authnuzhat.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RegistrationService {

    private final AuthService authService;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;
    private final OrganizationRepository organizationRepository;

    public RegistrationService(AuthService authService,
                               TeacherRepository teacherRepository, OrganizationRepository organizationRepository, AdminRepository adminRepository) {
        this.authService = authService;
        this.teacherRepository = teacherRepository;
        this.organizationRepository=organizationRepository;
        this.adminRepository=adminRepository;
    }

    @Transactional
    public void registerTeacher(TeacherRegistrationRequest signupRequest) {
        // Register user with ROLE_TEACHER
        User user = authService.registerUserWithRoles(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                signupRequest.getPassword(),
                Set.of("teacher") // Maps to "ROLE_TEACHER"
        );

        // Create and populate the Teacher entity
        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setName(signupRequest.getName());
        Organization organization = organizationRepository.findById(signupRequest.getOrganizationId())
                .orElse(null);
        teacher.setOrganization(organization);
        teacher.setContactNumber(signupRequest.getContactNumber());
        teacher.setDepartment(signupRequest.getDepartment());
        teacher.setQualifications(signupRequest.getQualifications());
        teacher.setJoinDate(signupRequest.getJoinDate());

        teacherRepository.save(teacher);
    }

    @Transactional
    public void registerAdmin(AdminRegistrationRequest signupRequest) {
        // Register user with ROLE_ADMIN
        User user = authService.registerUserWithRoles(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                signupRequest.getPassword(),
                Set.of("admin") // Maps to ROLE_ADMIN
        );

        // Create and populate the Admin entity
        Admin admin = new Admin();
        admin.setUser(user);
        admin.setName(signupRequest.getName());
        admin.setAdminRoleTitle(signupRequest.getAdminRoleTitle());
        admin.setContactNumber(signupRequest.getContactNumber());
        admin.setJoinedDate(signupRequest.getJoinDate());
        admin.setNotes(signupRequest.getNotes());

        adminRepository.save(admin);
    }

}
