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


import com.example.authnuzhat.models.McqOptions;

import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import org.apache.poi.xwpf.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

    @Override
    public List<ExamPaperResponseDTO> findByCreatedBy(String username) {
        List<ExamPaper> papers = examPaperRepository.findByCreatedBy(username);
        return papers.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] generatePdf(Long examPaperId) {
        ExamPaper paper = examPaperRepository.findById(examPaperId)
                .orElseThrow(() -> new RuntimeException("Exam paper not found"));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document();
            PdfWriter.getInstance(doc, out);
            doc.open();
            doc.add(new Paragraph("Exam Title: " + paper.getTitle()));
            doc.add(new Paragraph("Created By: " + paper.getCreatedBy()));
            doc.add(new Paragraph("Duration: " + paper.getDuration()));
            doc.add(new Paragraph(" "));

            for (ExamPaperQuestion epq : paper.getExamPaperQuestions()) {
                doc.add(new Paragraph("Q: " + epq.getQuestion().getQuestionText()));
                if ("MCQ".equalsIgnoreCase(epq.getQuestion().getQuestionType().getName())) {
                    for (McqOptions opt : epq.getQuestion().getMcqOptions()) {
                        doc.add(new Paragraph(" - " + opt.getOptionText()));
                    }
                }
                doc.add(new Paragraph("Mark: " + epq.getMark()));
                doc.add(new Paragraph(" "));
            }

            doc.close();
            return out.toByteArray();
        } catch (IOException | DocumentException e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    @Override
    public byte[] generateDocx(Long examPaperId) {
        ExamPaper paper = examPaperRepository.findById(examPaperId)
                .orElseThrow(() -> new RuntimeException("Exam paper not found"));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            XWPFDocument doc = new XWPFDocument();
            XWPFParagraph title = doc.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Exam Title: " + paper.getTitle());
            titleRun.setBold(true);
            titleRun.setFontSize(14);

            XWPFParagraph info = doc.createParagraph();
            XWPFRun infoRun = info.createRun();
            infoRun.setText("Created By: " + paper.getCreatedBy());
            infoRun.addBreak();
            infoRun.setText("Duration: " + paper.getDuration());

            for (ExamPaperQuestion epq : paper.getExamPaperQuestions()) {
                XWPFParagraph qPara = doc.createParagraph();
                XWPFRun qRun = qPara.createRun();
                qRun.addBreak();
                qRun.setText("Q: " + epq.getQuestion().getQuestionText());

                if ("MCQ".equalsIgnoreCase(epq.getQuestion().getQuestionType().getName())) {
                    for (McqOptions opt : epq.getQuestion().getMcqOptions()) {
                        XWPFParagraph optPara = doc.createParagraph();
                        XWPFRun optRun = optPara.createRun();
                        optRun.setText(" - " + opt.getOptionText());
                    }
                }

                XWPFParagraph markPara = doc.createParagraph();
                XWPFRun markRun = markPara.createRun();
                markRun.setText("Mark: " + epq.getMark());
            }

            doc.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate DOCX", e);
        }
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
