package com.example.authnuzhat.utility;

import com.example.authnuzhat.models.ExamPaper;
import com.example.authnuzhat.models.ExamPaperQuestion;
import com.example.authnuzhat.models.McqOptions;
import com.example.authnuzhat.models.Question;
import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import org.apache.poi.xwpf.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExamPaperExportUtil {

    public static byte[] generatePdf(ExamPaper examPaper) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font questionFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph("Exam Title: " + examPaper.getTitle(), titleFont));
        document.add(new Paragraph("Created By: " + examPaper.getCreatedBy()));
        document.add(new Paragraph("Duration: " + examPaper.getDuration()));
        document.add(new Paragraph("\n"));

        int qno = 1;
        for (ExamPaperQuestion epq : examPaper.getExamPaperQuestions()) {
            Question q = epq.getQuestion();
            document.add(new Paragraph(qno++ + ". " + q.getQuestionText() + " (" + epq.getMark() + " marks)", questionFont));
            if ("MCQ".equalsIgnoreCase(q.getQuestionType().getName())) {
                List<McqOptions> options = q.getMcqOptions();
                for (McqOptions opt : options) {
                    document.add(new Paragraph("   - " + opt.getOptionText()));
                }
            }
            document.add(new Paragraph("\n"));
        }

        document.close();
        return out.toByteArray();
    }

    public static byte[] generateDocx(ExamPaper examPaper) throws IOException {
        XWPFDocument document = new XWPFDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        XWPFParagraph title = document.createParagraph();
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Exam Title: " + examPaper.getTitle());
        titleRun.setBold(true);
        titleRun.setFontSize(16);

        XWPFParagraph meta = document.createParagraph();
        XWPFRun metaRun = meta.createRun();
        metaRun.setText("Created By: " + examPaper.getCreatedBy() + ", Duration: " + examPaper.getDuration());

        int qno = 1;
        for (ExamPaperQuestion epq : examPaper.getExamPaperQuestions()) {
            Question q = epq.getQuestion();
            XWPFParagraph qPara = document.createParagraph();
            XWPFRun qRun = qPara.createRun();
            qRun.setText(qno++ + ". " + q.getQuestionText() + " (" + epq.getMark() + " marks)");

            if ("MCQ".equalsIgnoreCase(q.getQuestionType().getName())) {
                for (McqOptions opt : q.getMcqOptions()) {
                    XWPFParagraph optPara = document.createParagraph();
                    XWPFRun optRun = optPara.createRun();
                    optRun.setText("   - " + opt.getOptionText());
                }
            }
        }

        document.write(out);
        return out.toByteArray();
    }
}
