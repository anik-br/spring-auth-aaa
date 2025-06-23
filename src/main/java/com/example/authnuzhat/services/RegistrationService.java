package com.example.authnuzhat.services;

import com.example.authnuzhat.dto.request.AdminRegistrationRequest;
import com.example.authnuzhat.dto.request.AdminUpdateRequest;
import com.example.authnuzhat.dto.request.TeacherRegistrationRequest;
import com.example.authnuzhat.models.Admin;
import com.example.authnuzhat.models.Organization;
import com.example.authnuzhat.models.Teacher;
import com.example.authnuzhat.models.User;
import com.example.authnuzhat.repository.AdminRepository;
import com.example.authnuzhat.repository.OrganizationRepository;
import com.example.authnuzhat.repository.TeacherRepository;
import com.example.authnuzhat.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RegistrationService {

    private final AuthService authService;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public RegistrationService(AuthService authService,
                               TeacherRepository teacherRepository, OrganizationRepository organizationRepository, AdminRepository adminRepository, UserRepository userRepository) {
        this.authService = authService;
        this.teacherRepository = teacherRepository;
        this.organizationRepository=organizationRepository;
        this.adminRepository=adminRepository;
        this.userRepository=userRepository;
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

    @Transactional
    public void updateAdmin(Long adminId, AdminUpdateRequest updateRequest) { // Changed DTO type
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        // Update Admin entity details only if the fields are provided in the request
        if (updateRequest.getName() != null) {
            admin.setName(updateRequest.getName());
        }
        if (updateRequest.getAdminRoleTitle() != null) {
            admin.setAdminRoleTitle(updateRequest.getAdminRoleTitle());
        }
        if (updateRequest.getContactNumber() != null) {
            admin.setContactNumber(updateRequest.getContactNumber());
        }
        if (updateRequest.getJoinDate() != null) {
            admin.setJoinedDate(updateRequest.getJoinDate());
        }
        if (updateRequest.getNotes() != null) {
            admin.setNotes(updateRequest.getNotes());
        }
        // IMPORTANT: No update to User details (username, email, password) here.
        // If you need to update user details, it should be a separate service call
        // (e.g., authService.updateUserDetails(admin.getUser().getId(), updateRequest.getUsername(), ...))
        // or a new dedicated DTO for user profile updates.

        adminRepository.save(admin);
    }

    @Transactional
    public void deleteAdmin(Long adminId) {
        // Find the admin. If the Admin entity has cascade = CascadeType.ALL on the User relationship,
        // deleting the Admin will automatically delete the associated User.
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        // Because of cascade = CascadeType.ALL on Admin.user, this single line
        // will now delete both the Admin and its associated User.
        adminRepository.delete(admin);
    }
}