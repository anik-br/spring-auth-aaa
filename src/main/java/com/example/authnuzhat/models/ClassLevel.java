package com.example.authnuzhat.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "class_level")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClassLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Class one , class tw o, ssc, hsc ....
    @Column(unique = true, nullable = false, length = 100)
    private String name;

    // Cambridge, Oxford, English, Bangla etc
    @Column(length = 100)
    private String curriculum;

    // Primary level, secondary level etc
    @Column(length = 50)
    private String educationLevel;

    @OneToMany(mappedBy = "classLevel", cascade = CascadeType.ALL)
    private List<Question> questions;
}