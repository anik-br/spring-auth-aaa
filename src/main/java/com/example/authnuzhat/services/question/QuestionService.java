package com.example.authnuzhat.services.question;

import com.example.authnuzhat.dto.request.AnswerRequestDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
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
    public QuestionResponseDTO createQuestion(QuestionRequestDTO questionRequestDTO, MultipartFile[] images, MultipartFile[] optionImages) throws IOException {
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

        //  Map option image files using original filename

        Map<String, MultipartFile> optionImageMap = new HashMap<>();
        if (optionImages != null) {
            for (MultipartFile image : optionImages) {
                // key = original filename
                optionImageMap.put(image.getOriginalFilename(), image);
            }
        }

        // Save MCQ options (if any)
        List<McqOptions> savedMcqOptions = new ArrayList<>();
        if (questionRequestDTO.getMcqOptions() != null) {

            for (McqOptionRequestDTO mcqOptionRequestDTO : questionRequestDTO.getMcqOptions()) {
                String imageUrl = null;

                // If the option has an imageKey, try to find matching image and upload it
                if (mcqOptionRequestDTO.getImageKey() != null && optionImageMap.containsKey(mcqOptionRequestDTO.getImageKey())) {
                    MultipartFile imageFile = optionImageMap.get(mcqOptionRequestDTO.getImageKey());

                    // Use your ImageService to save and get the image URL
                    ImageRequestDTO imageRequestDTO = new ImageRequestDTO();
                    imageRequestDTO.setQuestionId(savedQuestion.getId()); // optional, if image service needs it
                    imageRequestDTO.setFile(imageFile);

                    imageUrl = imageService.createImage(imageRequestDTO).getUrl();
                }

                McqOptions mcqOption = getMcqOptions(mcqOptionRequestDTO, savedQuestion, imageUrl);
                McqOptions savedMcqOption = mcqOptionsRepository.save(mcqOption);
                savedMcqOptions.add(savedMcqOption);
            }
        }

        // Save Answer (if any)

        if (questionType.getName().equalsIgnoreCase("MCQ")) {
            Answer answer = new Answer();
            for (McqOptions mcqOption : savedMcqOptions) {
                if (mcqOption.isCorrect()) {
                    answer.getCorrectOptions().add(mcqOption);
                }
            }
            answer.setQuestion(savedQuestion); // Important to set the relationship
            answerRepository.save(answer);
        }
        else if (questionRequestDTO.getAnswer() != null) {
            Answer answer = new Answer();
            AnswerRequestDTO answerDTO = questionRequestDTO.getAnswer();

            answer.setCorrectText(answerDTO.getCorrectText());
            answer.setCorrectBoolean(answerDTO.getCorrectBoolean());
            answer.setNumericalValue(answerDTO.getNumericalValue());
            answer.setNumericalTolerance(answerDTO.getNumericalTolerance());
            answer.setScore(answerDTO.getScore());
            answer.setAllowPartialScoring(answerDTO.getAllowPartialScoring());
            answer.setNegativeScore(answerDTO.getNegativeScore());
            answer.setQuestion(savedQuestion);

            answerRepository.save(answer);
        }


        // Save Images using ImageService
        if (images != null) {
            for (MultipartFile image : images) {
                ImageRequestDTO imageRequestDTO = new ImageRequestDTO();
                imageRequestDTO.setQuestionId(savedQuestion.getId());
                imageRequestDTO.setFile(image);
                imageService.createImage(imageRequestDTO); // Image service handles the upload
            }
        }
        // Map Entity to DTO
        return mapToResponseDTO(savedQuestion);
    }


    private McqOptions getMcqOptions(McqOptionRequestDTO mcqOptionRequestDTO, Question savedQuestion, String imageUrl) {
        McqOptions mcqOption = new McqOptions();
        mcqOption.setOptionText(mcqOptionRequestDTO.getOptionText());
        mcqOption.setCorrect(mcqOptionRequestDTO.isCorrect());
        mcqOption.setSortOrder(mcqOptionRequestDTO.getSortOrder());

        if (imageUrl != null) {
            mcqOption.setImageUrl(imageUrl);
        } else if (mcqOptionRequestDTO.getImageUrl() != null) {
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

    // Updating a Question
    @Transactional
    @Override
    public QuestionResponseDTO updateQuestion(Long questionId, QuestionRequestDTO questionRequestDTO, MultipartFile[] images, MultipartFile[] optionImages) throws IOException {
        // Fetch existing question
        Question existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + questionId));

        // Fetch related entities
        QuestionType questionType = questionTypeRepository.findById(questionRequestDTO.getQuestionTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("QuestionType not found with id: " + questionRequestDTO.getQuestionTypeId()));
        DifficultyLevel difficultyLevel = difficultyLevelRepository.findById(questionRequestDTO.getDifficultyLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("DifficultyLevel not found with id: " + questionRequestDTO.getDifficultyLevelId()));
        Subject subject = subjectRepository.findById(questionRequestDTO.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + questionRequestDTO.getSubjectId()));
        ClassLevel classLevel = classLevelRepository.findById(questionRequestDTO.getClassLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("ClassLevel not found with id: " + questionRequestDTO.getClassLevelId()));

        // Update question fields
        existingQuestion.setQuestionText(questionRequestDTO.getQuestionText());
        existingQuestion.setQuestionType(questionType);
        existingQuestion.setDifficultyLevel(difficultyLevel);
        existingQuestion.setSubject(subject);
        existingQuestion.setClassLevel(classLevel);
        existingQuestion.setImportance(questionRequestDTO.getImportance());
        existingQuestion.setScore(questionRequestDTO.getScore());
        existingQuestion.setModifiedBy(questionRequestDTO.getModifiedBy());
        existingQuestion.setActive(questionRequestDTO.isActive());
        existingQuestion.setExplanation(questionRequestDTO.getExplanation());

        Question updatedQuestion = questionRepository.save(existingQuestion);



        // Delete existing MCQ options and their images (if necessary)
        List<McqOptions> existingMcqOptions = mcqOptionsRepository.findByQuestionId(questionId);
        existingMcqOptions.forEach(option -> {
            if (option.getImageUrl() != null) {
                imageService.deleteImageByUrl(option.getImageUrl());
            }
        });

        // Clear old Answer correctOptions
        Optional<Answer> existingAnswerMcqOpt = answerRepository.findByQuestionId(questionId);
        existingAnswerMcqOpt.ifPresent(answer -> {
            answer.getCorrectOptions().clear();
            answerRepository.saveAndFlush(answer);  // IMPORTANT
        });

        // 2. Clear old MCQ options
        updatedQuestion.getMcqOptions().clear();
        questionRepository.saveAndFlush(updatedQuestion);  // IMPORTANT



        // Process new MCQ options
        List<McqOptions> savedMcqOptions = new ArrayList<>();
        Map<String, MultipartFile> optionImageMap = new HashMap<>();
        if (optionImages != null) {
            Arrays.stream(optionImages).forEach(image ->
                    optionImageMap.put(image.getOriginalFilename(), image));
        }

        if (questionRequestDTO.getMcqOptions() != null) {
            for (McqOptionRequestDTO mcqOptionDTO : questionRequestDTO.getMcqOptions()) {
                String imageUrl = null;
                if (mcqOptionDTO.getImageKey() != null && optionImageMap.containsKey(mcqOptionDTO.getImageKey())) {
                    MultipartFile imageFile = optionImageMap.get(mcqOptionDTO.getImageKey());
                    ImageRequestDTO imageReqDTO = new ImageRequestDTO();
                    imageReqDTO.setQuestionId(questionId);
                    imageReqDTO.setFile(imageFile);
                    imageUrl = imageService.createImage(imageReqDTO).getUrl();
                }
                McqOptions mcqOption = new McqOptions();
                mcqOption.setOptionText(mcqOptionDTO.getOptionText());
                mcqOption.setCorrect(mcqOptionDTO.isCorrect());
                mcqOption.setSortOrder(mcqOptionDTO.getSortOrder());
                mcqOption.setImageUrl(imageUrl != null ? imageUrl : mcqOptionDTO.getImageUrl());
                mcqOption.setQuestion(updatedQuestion);
                savedMcqOptions.add(mcqOptionsRepository.save(mcqOption));
            }
        }

        // Update or create Answer
        Optional<Answer> existingAnswerOpt = answerRepository.findByQuestionId(questionId);
        Answer answer = existingAnswerOpt.orElse(new Answer());

        if (questionType.getName().equalsIgnoreCase("MCQ")) {
            answer.getCorrectOptions().clear();
            savedMcqOptions.stream()
                    .filter(McqOptions::isCorrect)
                    .forEach(answer.getCorrectOptions()::add);
        } else if (questionRequestDTO.getAnswer() != null) {
            AnswerRequestDTO answerDTO = questionRequestDTO.getAnswer();
            answer.setCorrectText(answerDTO.getCorrectText());
            answer.setCorrectBoolean(answerDTO.getCorrectBoolean());
            answer.setNumericalValue(answerDTO.getNumericalValue());
            answer.setNumericalTolerance(answerDTO.getNumericalTolerance());
            answer.setScore(answerDTO.getScore());
            answer.setAllowPartialScoring(answerDTO.getAllowPartialScoring());
            answer.setNegativeScore(answerDTO.getNegativeScore());
        }

        answer.setQuestion(updatedQuestion);
        answerRepository.save(answer);

        // Delete existing images for the question and update new ones from the request
        // If new images are provided, delete existing ones and upload the new ones

        if (images != null && Arrays.stream(images).anyMatch(image -> !image.isEmpty())) {
            imageService.deleteByQuestionId(questionId);
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    ImageRequestDTO imageReqDTO = new ImageRequestDTO();
                    imageReqDTO.setQuestionId(questionId);
                    imageReqDTO.setFile(image);
                    imageService.createImage(imageReqDTO);
                }
            }
        }


        return mapToResponseDTO(updatedQuestion);
    }

    // Delete a Question and associative everythings
    @Override
    public void deleteQuestion(Long id) {
        // Check if the question exists
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));

        // Delete question-level images (DB + storage)
        imageService.deleteByQuestionId(id);

        // Delete MCQ option images (only file storage, as DB is auto-handled via cascade)
        for (McqOptions option : question.getMcqOptions()) {
            if (option.getImageUrl() != null) {
                imageService.deleteImageByUrl(option.getImageUrl());
            }
        }

        // Delete the question (cascades to MCQ options, answer, and images in DB)
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