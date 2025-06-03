package com.example.authnuzhat.models;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String questionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_type_id", nullable = false)
    private QuestionType questionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "difficulty_level_id", nullable = false)
    private DifficultyLevel difficultyLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_level_id", nullable = false)
    private ClassLevel classLevel;

    // Importance will be set from 1 to 5
    //
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionImportance importance;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<McqOptions> mcqOptions = new ArrayList<>(); // List of options for the question

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Answer answer;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>(); // List of images associated with the question

    private Integer score=1; // Marks for the question, default value is 1
    private String createdBy;
    private String modifiedBy;
    private boolean active;
    private String explanation;

    private LocalDateTime createdAt = LocalDateTime.now(); // Set creation time only once

    private LocalDateTime updatedAt; // Will be set on updates

    @PreUpdate
    private void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now(); // Auto-updates before saving changes
    }

    private LocalDateTime deletedAt; // Time of the soft delete

}