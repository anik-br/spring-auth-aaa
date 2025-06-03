package com.example.authnuzhat.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subject")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Subject parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Subject> children = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>(); // Initialize the list

    // Helper method to add a child subject
    public void addChild(Subject child) {
        children.add(child);
        child.setParent(this);
    }

    // Helper method to remove a child subject
    public void removeChild(Subject child) {
        children.remove(child);
        child.setParent(null);
    }
}