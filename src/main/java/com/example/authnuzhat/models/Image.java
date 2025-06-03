package com.example.authnuzhat.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String url; // URL or path to the image


    private String description; // Description of the image

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question; // Reference to the associated question

    private Integer imageOrder; // Order of the image if multiple images are associated with a question

}