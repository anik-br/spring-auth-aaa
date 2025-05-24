package com.example.authnuzhat.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    @Id
    private Long id; // Use the same ID as the User entity for 1-to-1 relationship

    private String Name;

    @OneToOne
    @MapsId // Maps the primary key of the owning entity (User) to this entity
    @JoinColumn(name = "user_id") // Optional, but good for clarity
    private User user;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    //private String organization;
    private String contactNumber;
    private String department;
    private String qualifications;

    private LocalDate joinDate;

}