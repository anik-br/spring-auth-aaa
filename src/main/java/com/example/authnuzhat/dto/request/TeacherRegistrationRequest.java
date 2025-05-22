package com.example.authnuzhat.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class TeacherRegistrationRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 1, max = 40)
    private String password;
    private Long organizationId; // ID of the selected organization


    private String contactNumber;
    private String department;
    private String qualifications;

    private LocalDate joinDate;
}

