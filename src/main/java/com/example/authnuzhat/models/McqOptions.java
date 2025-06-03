package com.example.authnuzhat.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mcq_option") // Changed to avoid reserved keyword conflict
@Data
@NoArgsConstructor
@AllArgsConstructor
public class McqOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the option

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question; // The question this option belongs to

    @Column(columnDefinition = "TEXT", nullable = false)
    private String optionText; // The text of the multiple-choice option

    private boolean correct = false; // Indicates if this option is correct (default: false)

    private Integer sortOrder; // Determines display order of options (nullable)
    private String imageUrl; // URL of the image for the option (nullable)

}