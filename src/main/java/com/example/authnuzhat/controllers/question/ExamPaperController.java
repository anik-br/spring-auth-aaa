package com.example.authnuzhat.controllers.question;

import com.example.authnuzhat.dto.request.ExamPaperRequestDTO;
import com.example.authnuzhat.dto.response.ExamPaperResponseDTO;
import com.example.authnuzhat.models.ExamPaper;
import com.example.authnuzhat.services.exam_paper.ExamPaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exam-papers")
@RequiredArgsConstructor
public class ExamPaperController {

    private final ExamPaperService examPaperService;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_EXAM_PAPER')")
    public ResponseEntity<ExamPaperResponseDTO> createExamPaper(@RequestBody ExamPaperRequestDTO dto) {
        return ResponseEntity.ok(examPaperService.createExamPaper(dto));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CREATE_EXAM_PAPER')")
    public ResponseEntity<List<ExamPaperResponseDTO>> getAll() {
        return ResponseEntity.ok(examPaperService.getAllExamPapers());
    }

    @GetMapping("/by-creator")
    @PreAuthorize("hasAuthority('CREATE_EXAM_PAPER')")
    public ResponseEntity<List<ExamPaperResponseDTO>> getByCreatedBy(@RequestParam String username) {
        List<ExamPaperResponseDTO> dtos = examPaperService.findByCreatedBy(username);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/exam-papers/{id}/download/pdf")
    @PreAuthorize("hasAuthority('CREATE_EXAM_PAPER')")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        byte[] pdfData = examPaperService.generatePdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exam-paper-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
    }

    @GetMapping("/exam-papers/{id}/download/docx")
    @PreAuthorize("hasAuthority('CREATE_EXAM_PAPER')")
    public ResponseEntity<byte[]> downloadDocx(@PathVariable Long id) {
        byte[] docxData = examPaperService.generateDocx(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exam-paper-" + id + ".docx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(docxData);
    }


}
