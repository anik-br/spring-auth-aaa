package com.example.authnuzhat.services.exam_paper;

import com.example.authnuzhat.dto.request.ExamPaperRequestDTO;
import com.example.authnuzhat.dto.response.ExamPaperResponseDTO;

import java.util.List;

public interface IExamPaperService {
    ExamPaperResponseDTO createExamPaper(ExamPaperRequestDTO requestDTO);
    List<ExamPaperResponseDTO> getAllExamPapers();
    byte[] generatePdf(Long examPaperId);
    byte[] generateDocx(Long examPaperId);
}

