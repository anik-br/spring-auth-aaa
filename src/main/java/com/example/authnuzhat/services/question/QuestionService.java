package com.example.authnuzhat.services.question;

import com.example.authnuzhat.dto.request.ImageRequestDTO;
import com.example.authnuzhat.dto.request.McqOptionRequestDTO;
import com.example.authnuzhat.dto.request.QuestionRequestDTO;
import com.example.authnuzhat.dto.response.AnswerResponseDTO;
import com.example.authnuzhat.dto.response.ImageResponseDTO;
import com.example.authnuzhat.dto.response.McqOptionResponseDTO;
import com.example.authnuzhat.dto.response.QuestionResponseDTO;
import com.example.authnuzhat.exception.ResourceNotFoundException;
import com.example.authnuzhat.models.*;
import com.example.authnuzhat.repository.*;
import com.example.authnuzhat.services.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService{
    private final QuestionRepository questionRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final DifficultyLevelRepository difficultyLevelRepository;
    private final SubjectRepository subjectRepository;
    private final ClassLevelRepository classLevelRepository;
    private final McqOptionsRepository mcqOptionsRepository;
    private final AnswerRepository answerRepository;
    private final IImageService imageService;



    @Override
    public QuestionResponseDTO createQuestion(QuestionRequestDTO questionRequestDTO) {
        // Fetch related entities or throw exceptions if not found
        QuestionType questionType = questionTypeRepository.findById(questionRequestDTO.getQuestionTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("QuestionType not found with id: " + questionRequestDTO.getQuestionTypeId()));

        DifficultyLevel difficultyLevel = difficultyLevelRepository.findById(questionRequestDTO.getDifficultyLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("DifficultyLevel not found with id: " + questionRequestDTO.getDifficultyLevelId()));

        Subject subject = subjectRepository.findById(questionRequestDTO.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + questionRequestDTO.getSubjectId()));

        ClassLevel classLevel = classLevelRepository.findById(questionRequestDTO.getClassLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("ClassLevel not found with id: " + questionRequestDTO.getClassLevelId()));


        // Map DTO to Entity
        Question question = new Question();
        question.setQuestionText(questionRequestDTO.getQuestionText());
        question.setQuestionType(questionType);
        question.setDifficultyLevel(difficultyLevel);
        question.setSubject(subject);
        question.setClassLevel(classLevel);
        question.setImportance(questionRequestDTO.getImportance());
        question.setScore(questionRequestDTO.getScore());
        question.setCreatedBy(questionRequestDTO.getCreatedBy());
        question.setModifiedBy(questionRequestDTO.getModifiedBy());
        question.setActive(questionRequestDTO.isActive());
        question.setExplanation(questionRequestDTO.getExplanation());

        // Save to database
        Question savedQuestion = questionRepository.save(question);

//        if (questionRequestDTO.getMcqOptions() != null) {
//            for (McqOptionRequestDTO mcqOptionRequestDTO : questionRequestDTO.getMcqOptions()) {
//                McqOptions mcqOption = getMcqOptions(mcqOptionRequestDTO, savedQuestion);
//                mcqOptionsRepository.save(mcqOption);
//            }
//        }

        // Save MCQ options (if any)
        List<McqOptions> savedMcqOptions = new ArrayList<>();
        if (questionRequestDTO.getMcqOptions() != null) {
            for (McqOptionRequestDTO mcqOptionRequestDTO : questionRequestDTO.getMcqOptions()) {
                McqOptions mcqOption = getMcqOptions(mcqOptionRequestDTO, savedQuestion);
                McqOptions savedMcqOption = mcqOptionsRepository.save(mcqOption);
                savedMcqOptions.add(savedMcqOption);
            }
        }

        // Save Answer (if any)
        // Save Answer (if any)
        if (questionRequestDTO.getAnswer() != null) {
            Answer answer = new Answer();
            answer.setCorrectText(questionRequestDTO.getAnswer().getCorrectText());
            answer.setCorrectBoolean(questionRequestDTO.getAnswer().getCorrectBoolean());
            answer.setNumericalValue(questionRequestDTO.getAnswer().getNumericalValue());
            answer.setNumericalTolerance(questionRequestDTO.getAnswer().getNumericalTolerance());
            answer.setScore(questionRequestDTO.getAnswer().getScore());
            answer.setAllowPartialScoring(questionRequestDTO.getAnswer().getAllowPartialScoring());
            answer.setNegativeScore(questionRequestDTO.getAnswer().getNegativeScore());
            answer.setQuestion(savedQuestion);



            // For MCQ questions, link correct options from savedMcqOptions
            if (questionType.getName().equalsIgnoreCase("MCQ")) {
                for (McqOptions mcqOption : savedMcqOptions) {
                    if (mcqOption.isCorrect()) {
                        answer.getCorrectOptions().add(mcqOption);
                    }
                }
            }

            answerRepository.save(answer);
        }


        // Save Images (if any) using IImageService
        if (questionRequestDTO.getImages() != null) {
            for (ImageRequestDTO imageRequestDTO : questionRequestDTO.getImages()) {
                imageRequestDTO.setQuestionId(savedQuestion.getId()); // Set the question ID
                try {
                    imageService.createImage(imageRequestDTO); // Use IImageService to create the image
                } catch (IOException e) {
                    // Handle IOException (e.g., log it or rethrow it as a runtime exception)
                    throw new RuntimeException("Error while saving image", e);
                }
            }
        }
        // Map Entity to DTO
        return mapToResponseDTO(savedQuestion);
    }


    private McqOptions getMcqOptions(McqOptionRequestDTO mcqOptionRequestDTO, Question savedQuestion) {
        McqOptions mcqOption = new McqOptions();
        mcqOption.setOptionText(mcqOptionRequestDTO.getOptionText());
        mcqOption.setCorrect(mcqOptionRequestDTO.isCorrect());
        mcqOption.setSortOrder(mcqOptionRequestDTO.getSortOrder());
        if (mcqOptionRequestDTO.getImageUrl()!=null){
            mcqOption.setImageUrl(mcqOptionRequestDTO.getImageUrl());
        }
        mcqOption.setQuestion(savedQuestion);
        return mcqOption;
    }

    @Override
    public List<QuestionResponseDTO> getAllQuestions() {
        // Fetch all Questions from the database
        List<Question> questions = questionRepository.findAll();

        // Map Entities to DTOs
        return questions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionResponseDTO getQuestionById(Long id) {
        // Fetch Question by ID or throw an exception if not found
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));

        // Map Entity to DTO
        return mapToResponseDTO(question);
    }

    @Override
    public QuestionResponseDTO updateQuestion(Long id, QuestionRequestDTO questionRequestDTO) {
        // Fetch Question by ID or throw an exception if not found
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));

        // Update fields
        question.setQuestionText(questionRequestDTO.getQuestionText());
        question.setImportance(questionRequestDTO.getImportance());
        question.setScore(questionRequestDTO.getScore());
        question.setModifiedBy(questionRequestDTO.getModifiedBy());
        question.setActive(questionRequestDTO.isActive());
        question.setExplanation(questionRequestDTO.getExplanation());

        // Save updated entity
        Question updatedQuestion = questionRepository.save(question);

        // Map Entity to DTO
        return mapToResponseDTO(updatedQuestion);
    }

    @Override
    public void deleteQuestion(Long id) {
        // Check if Question exists
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found with id: " + id);
        }

        // Delete Question
        questionRepository.deleteById(id);

    }

    @Override
    public List<QuestionResponseDTO> getQuestionsBySubjectId(Long subjectId) {
        // Fetch all Questions by Subject ID
        List<Question> questions = questionRepository.findBySubjectId(subjectId);

        // Map Entities to DTOs
        return questions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionResponseDTO> getQuestionsByClassLevelId(Long classLevelId) {
        // Fetch all Questions by Class Level ID
        List<Question> questions = questionRepository.findByClassLevelId(classLevelId);

        // Map Entities to DTOs
        return questions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionResponseDTO> getQuestionsByDifficultyLevelId(Long difficultyLevelId) {
        // Fetch all Questions by Difficulty Level ID
        List<Question> questions = questionRepository.findByDifficultyLevelId(difficultyLevelId);

        // Map Entities to DTOs
        return questions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionResponseDTO> getQuestionsByQuestionTypeId(Long questionTypeId) {
        // Fetch all Questions by Question Type ID
        List<Question> questions = questionRepository.findByQuestionTypeId(questionTypeId);

        // Map Entities to DTOs
        return questions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionResponseDTO> getActiveQuestions() {
        // Fetch all active Questions
        List<Question> questions = questionRepository.findByActiveTrue();

        // Map Entities to DTOs
        return questions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Helper method to map Entity to DTO
    private QuestionResponseDTO mapToResponseDTO(Question question) {
        return new QuestionResponseDTO(
                question.getId(),
                question.getQuestionText(),
                question.getQuestionType().getId(),
                question.getDifficultyLevel().getId(),
                question.getSubject().getId(),
                question.getClassLevel().getId(),
                question.getImportance(),
                question.getMcqOptions().stream()
                        .map(mcqOption -> new McqOptionResponseDTO(
                                mcqOption.getId(),
                                mcqOption.getOptionText(),
                                mcqOption.isCorrect(),
                                mcqOption.getSortOrder(),
                                mcqOption.getImageUrl()
                        ))
                        .collect(Collectors.toList()),
                question.getAnswer() != null ? new AnswerResponseDTO(
                        question.getAnswer().getId(),
                        question.getAnswer().getCorrectOptions().stream()
                                .map(McqOptions::getId).toList(),

                        question.getAnswer().getCorrectBoolean(),
                        question.getAnswer().getCorrectText(),
                        question.getAnswer().getNumericalValue(),
                        question.getAnswer().getNumericalTolerance(),
                        question.getAnswer().getScore(),
                        question.getAnswer().getAllowPartialScoring(),
                        question.getAnswer().getNegativeScore()
                ) : null,
                question.getImages().stream()
                        .map(image -> new ImageResponseDTO(
                                image.getId(),
                                image.getUrl(),
                                image.getDescription(),
                                image.getImageOrder(),
                                question.getId()
                        ))
                        .collect(Collectors.toList()),
                question.getScore(),
                question.getCreatedBy(),
                question.getModifiedBy(),
                question.isActive(),
                question.getExplanation(),
                question.getCreatedAt(),
                question.getUpdatedAt(),
                question.getDeletedAt()
        );
    }
}