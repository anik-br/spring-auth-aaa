package com.example.authnuzhat.dto.request;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRegistrationRequest {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name; // Add name field for User creation

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

    //private String organization;
    private String contactNumber;
    private String department;
    private String qualifications;

    private LocalDate joinDate;
}

