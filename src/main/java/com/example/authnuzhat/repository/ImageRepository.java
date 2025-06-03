package com.example.authnuzhat.repository;

import com.example.authnuzhat.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByQuestionId(Long questionId);

    void deleteByQuestionId(Long questionId);

    void deleteByUrl(String url);
}