package com.example.authnuzhat.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.util.List;

@Entity
@Table(name = "difficulty")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class DifficultyLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Easy, hard, medium, advanced
    @Column(nullable = false, unique = true, length = 50)
    private String level;

    @OneToMany(mappedBy = "difficultyLevel", cascade = CascadeType.ALL)
    private List<Question> questions;

}