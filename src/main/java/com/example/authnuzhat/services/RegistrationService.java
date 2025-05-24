package com.example.authnuzhat.services;

import com.example.authnuzhat.dto.request.TeacherRegistrationRequest;
import com.example.authnuzhat.models.Organization;
import com.example.authnuzhat.models.Teacher;
import com.example.authnuzhat.models.User;
import com.example.authnuzhat.repository.OrganizationRepository;
import com.example.authnuzhat.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RegistrationService {

    private final AuthService authService;
    private final TeacherRepository teacherRepository;
    private final OrganizationRepository organizationRepository;

    public RegistrationService(AuthService authService,
                               TeacherRepository teacherRepository, OrganizationRepository organizationRepository) {
        this.authService = authService;
        this.teacherRepository = teacherRepository;
        this.organizationRepository=organizationRepository;
    }

    @Transactional
    public void registerTeacher(TeacherRegistrationRequest signupRequest) {
        // Register user with ROLE_TEACHER
        User user = authService.registerUserWithRoles(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                signupRequest.getPassword(),
                Set.of("mod") // Maps to "ROLE_TEACHER"
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
}
