package nowire.space.learnromanian.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import nowire.space.learnromanian.model.Exercise;
import nowire.space.learnromanian.repository.ExerciseRepository;
import nowire.space.learnromanian.request.ExerciseRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExerciseService {

    private ExerciseRepository exerciseRepository;
    public ResponseEntity<String> createExercise(ExerciseRequest exerciseRequest) {
        if(exerciseRequest!=null) {
            Exercise exercise = Exercise.builder().name(exerciseRequest.getName())
                    .description(exerciseRequest.getDescription())
                    .exerciseType(exerciseRequest.getExerciseType()).build();
            exerciseRepository.save(exercise);
            return new ResponseEntity<>("exercise_created", HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>("someting went wrong !", HttpStatus.BAD_REQUEST);

        }

    }

    public ResponseEntity<String> getExercise(String name){
         String exercise = exerciseRepository.findByName(name).getExerciseType().getExercise();
         return new ResponseEntity<>(exercise,HttpStatus.OK);
    }
}
