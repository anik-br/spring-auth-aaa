package com.example.authnuzhat.services.questionType;

import com.example.authnuzhat.dto.request.QuestionTypeRequestDTO;
import com.example.authnuzhat.dto.response.QuestionTypeResponseDTO;
import com.example.authnuzhat.exception.ResourceNotFoundException;
import com.example.authnuzhat.models.QuestionType;
import com.example.authnuzhat.repository.QuestionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class QuestionTypeService implements IQuestionTypeService{
    private final QuestionTypeRepository questionTypeRepository;
    @Override
    public QuestionTypeResponseDTO createQuestionType(QuestionTypeRequestDTO questionTypeRequestDTO) {
        // Check if a QuestionType with the same name already exists
        if (questionTypeRepository.existsByName(questionTypeRequestDTO.getName())){
            throw new IllegalArgumentException("Question type with name '" + questionTypeRequestDTO.getName()+"' already exists");
        }

        //map dto to entity
        QuestionType questionType= new QuestionType();
        questionType.setName(questionTypeRequestDTO.getName());
        questionType.setDescription(questionTypeRequestDTO.getDescription());

        // Save to database
        QuestionType savedQuestionType= questionTypeRepository.save(questionType);
        // map entity to response dto
        return mapToResponseDTO(savedQuestionType);

    }

    @Override
    public List<QuestionTypeResponseDTO> getAllQuestionTypes() {
        // Fetch all QuestionTypes from the database
        List<QuestionType> questionTypes= questionTypeRepository.findAll();

        // map entities to DTOs
        return questionTypes.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

    }

    @Override
    public QuestionTypeResponseDTO getQuestionTypeById(Long id) {
        QuestionType questionType=questionTypeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Question type not found with id: "+ id));

        //Map entity to DTO

        return mapToResponseDTO(questionType);
    }

    @Override
    public QuestionTypeResponseDTO updateQuestionType(Long id, QuestionTypeRequestDTO questionTypeRequestDTO) {
        // Fetch QuestionType by ID or throw an exception if not found

        QuestionType questionType=questionTypeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Question type not found with id: "+ id));
        // update fields
        questionType.setName(questionTypeRequestDTO.getName());
        questionType.setDescription(questionTypeRequestDTO.getDescription());

        // save updated entity
        QuestionType updatedQuestionType= questionTypeRepository.save(questionType);
        // map entity to DTO
        return mapToResponseDTO(updatedQuestionType);

    }

    @Override
    public void deleteQuestionType(Long id) {
        // Check if the QuestionType exists
        if(!questionTypeRepository.existsById(id)){
            throw new ResourceNotFoundException("Question type not found with id: " + id);
        }
        // delete question type
        questionTypeRepository.deleteById(id);

    }
    // Helper method to map Entity to DTO
    private QuestionTypeResponseDTO mapToResponseDTO(QuestionType questionType){
        return new QuestionTypeResponseDTO(
                questionType.getId(),
                questionType.getName(),
                questionType.getDescription()
        );
    }

}