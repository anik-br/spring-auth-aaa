package com.example.authnuzhat.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) // orphanRemoval is good practice for OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    private String name;

    // Additional fields for an Admin (might be less specific, but still possible)
    private String adminRoleTitle; // e.g., "System Administrator", "Content Moderator"
    private String contactNumber;
    private LocalDate joinedDate;
    //private String status; // ACTIVE, INACTIVE etc

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private String notes;
}

