package nowire.space.learnromanian.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nowire.space.learnromanian.model.CompleteSentenceExercise;
import nowire.space.learnromanian.model.FreeWritingExercise;
import nowire.space.learnromanian.model.MultipleChoiceExercise;
import nowire.space.learnromanian.repository.CompleteSentenceExerciseRepository;
import nowire.space.learnromanian.repository.ExamRepository;
import nowire.space.learnromanian.repository.FreeWritingExerciseRepository;
import nowire.space.learnromanian.repository.MultipleChoiceExerciseRepository;
import nowire.space.learnromanian.request.CompleteSentenceRequest;
import nowire.space.learnromanian.request.FreeWritingRequest;
import nowire.space.learnromanian.request.MultipleChoiceRequest;
import nowire.space.learnromanian.util.Enum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExerciseService {

    private MultipleChoiceExerciseRepository multipleChoiceExerciseRepository;
    private FreeWritingExerciseRepository freeWritingExerciseRepository;
    private CompleteSentenceExerciseRepository completeSentenceExerciseRepository;

    @Secured({Enum.Role.ADMIN, Enum.Role.MODERATOR, Enum.Role.PROFESSOR})
    public ResponseEntity<String> createMultipleChoiceExercise(MultipleChoiceRequest multipleChoiceRequest) {
        if(multipleChoiceRequest!=null) {
            MultipleChoiceExercise exercise = MultipleChoiceExercise.builder()
                    .correctAnswer(multipleChoiceRequest.getCorrectAnswer())
                    .question(multipleChoiceRequest.getQuestion())
                    .build();
            multipleChoiceExerciseRepository.save(exercise);
            return new ResponseEntity<>("Multiple choice exercise was created !", HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>("something went wrong !", HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<String> createFreeWritingExercise(FreeWritingRequest exerciseRequest) {
        if (exerciseRequest !=null){
            FreeWritingExercise freeWritingExercise = FreeWritingExercise.builder().question(exerciseRequest.getQuestion())
                    .request(exerciseRequest.getRequest()).build();
            freeWritingExerciseRepository.save(freeWritingExercise);
            return new ResponseEntity<>("Free wording exercise was created", HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>("something went wrong !", HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<String> createCompleteSentenceExercise(CompleteSentenceRequest exerciseRequest) {
        if (exerciseRequest != null){
            CompleteSentenceExercise completeSentenceExercise = CompleteSentenceExercise.builder()
                    .question(exerciseRequest.getQuestion())
                    .sentence(exerciseRequest.getSentence())
                    .positionAnswers(exerciseRequest.getPositionAnswers())
                    .build();
            completeSentenceExerciseRepository.save(completeSentenceExercise);
            return new ResponseEntity<>("Complete sentence exercise was created", HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>("something went wrong !", HttpStatus.BAD_REQUEST);
        }

    }


//    public ResponseEntity<String> getExercise(String name){
//         String exercise = exerciseRepository.findByName(name).getExerciseType().getExercise();
//         return new ResponseEntity<>(exercise,HttpStatus.OK);
//    }
}
