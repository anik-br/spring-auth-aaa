package com.example.authnuzhat.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data // Lombok for getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateRequest {

    // Make these optional (no @NotBlank) if you don't always update them,
    // or keep them if they are mandatory for *any* update.
    // For this error, they should *not* be @NotBlank.
    @Size(max = 100) // Example size validation if desired
    private String name;

    @Size(max = 50) // Example size validation
    private String adminRoleTitle;

    @Size(max = 20) // Example size validation
    private String contactNumber;

    private LocalDate joinDate; // Note: typically 'joinDate' in request, 'joinedDate' in entity

    @Size(max = 500) // Example size validation
    private String notes;

    // IMPORTANT: Do NOT include username, email, password here with @NotBlank.
    // If you want to allow changing username/email/password, you'd have
    // separate methods/DTOs for that, as they are sensitive operations.
}