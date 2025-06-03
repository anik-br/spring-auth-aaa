package com.example.authnuzhat.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "answer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the answer

    @OneToOne
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    private Question question;

    @ManyToMany
    @JoinTable(
            name = "answer_option",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private Set<McqOptions> correctOptions = new HashSet<>(); // Initialize the Set// multiple options may be correct answer

    private Boolean correctBoolean; // Stores the correct answer for True/False questions

    @Lob
    private String correctText; // Stores correct text-based answers (short/long answers)

    private Double numericalValue; // Stores the correct numerical value for numerical questions

    private Double numericalTolerance; // Allowed margin of error for numerical answers

    private Double score; // Points assigned for answering this question correctly

    private Boolean allowPartialScoring = Boolean.FALSE; // Indicates if partial scoring is allowed

    private BigDecimal negativeScore = BigDecimal.ZERO; // Negative marking (if applicable)
}