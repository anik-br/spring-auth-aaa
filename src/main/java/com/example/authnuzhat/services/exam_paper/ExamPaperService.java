package com.example.authnuzhat.services.exam_paper;


import com.example.authnuzhat.dto.request.ExamPaperQuestionDTO;
import com.example.authnuzhat.dto.request.ExamPaperRequestDTO;
import com.example.authnuzhat.dto.response.ExamPaperResponseDTO;
import com.example.authnuzhat.exception.ResourceNotFoundException;
import com.example.authnuzhat.models.ExamPaper;
import com.example.authnuzhat.models.ExamPaperQuestion;
import com.example.authnuzhat.models.Question;
import com.example.authnuzhat.repository.ExamPaperQuestionRepository;
import com.example.authnuzhat.repository.ExamPaperRepository;
import com.example.authnuzhat.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamPaperService implements IExamPaperService {

    private final ExamPaperRepository examPaperRepository;
    private final QuestionRepository questionRepository;
    private final ExamPaperQuestionRepository examPaperQuestionRepository;

    @Override
    public ExamPaperResponseDTO createExamPaper(ExamPaperRequestDTO requestDTO) {
        ExamPaper examPaper = new ExamPaper();
        examPaper.setTitle(requestDTO.getTitle());
        examPaper.setCreatedBy(requestDTO.getCreatedBy());
        examPaper.setDuration(requestDTO.getDuration());
        examPaper.setCreatedAt(LocalDateTime.now());
        examPaper.setExamPaperQuestions(new ArrayList<>());

        // Save paper first to establish ID
        ExamPaper savedPaper = examPaperRepository.save(examPaper);

        for (ExamPaperQuestionDTO qdto : requestDTO.getQuestions()) {
            Question question = questionRepository.findById(qdto.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + qdto.getQuestionId()));

            ExamPaperQuestion epq = new ExamPaperQuestion();
            epq.setExamPaper(savedPaper);
            epq.setQuestion(question);
            epq.setMark(qdto.getMark());

            examPaperQuestionRepository.save(epq);
            savedPaper.getExamPaperQuestions().add(epq);
        }

        return mapToResponseDTO(savedPaper);
    }

    @Override
    public List<ExamPaperResponseDTO> getAllExamPapers() {
        return examPaperRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private ExamPaperResponseDTO mapToResponseDTO(ExamPaper paper) {
        List<ExamPaperQuestionDTO> questionDTOs = paper.getExamPaperQuestions()
                .stream()
                .map(q -> {
                    ExamPaperQuestionDTO dto = new ExamPaperQuestionDTO();
                    dto.setQuestionId(q.getQuestion().getId());
                    dto.setMark(q.getMark());
                    return dto;
                })
                .collect(Collectors.toList());

        ExamPaperResponseDTO dto = new ExamPaperResponseDTO();
        dto.setId(paper.getId());
        dto.setTitle(paper.getTitle());
        dto.setCreatedBy(paper.getCreatedBy());
        dto.setDuration(paper.getDuration());
        dto.setCreatedAt(paper.getCreatedAt());
        dto.setQuestions(questionDTOs);
        return dto;
    }
}
