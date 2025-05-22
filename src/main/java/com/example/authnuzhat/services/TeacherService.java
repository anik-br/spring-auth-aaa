package com.example.authnuzhat.services;

import com.example.authnuzhat.dto.request.TeacherRegistrationRequest;
import com.example.authnuzhat.models.Organization;
import com.example.authnuzhat.models.Teacher;
import com.example.authnuzhat.models.User;
import com.example.authnuzhat.payload.response.MessageResponse;
import com.example.authnuzhat.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserService userService; // handles user creation

    public MessageResponse registerTeacher(TeacherRegistrationRequest request) {

        if (teacherRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        // Create User and assign role
        User user = userService.createUserWithRole(request.getEmail(), request.getPassword(), "TEACHER");

        Teacher teacher = new Teacher();
        teacher.setName(request.getName());
        teacher.setEmail(request.getEmail());
        teacher.setOrganization(org);
        teacher.setUser(user);

        teacherRepository.save(teacher);

        return new MessageResponse("Teacher registered successfully.");
    }
}
