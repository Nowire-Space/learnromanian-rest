package nowire.space.learnromanian.controller;

import lombok.AllArgsConstructor;
import nowire.space.learnromanian.request.ExerciseRequest;
import nowire.space.learnromanian.service.ExerciseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/exercise")
public class ExerciseController {

    private ExerciseService exerciseService;

    @PostMapping("/create")
    public ResponseEntity<String> createExercise(@RequestBody ExerciseRequest exerciseRequest){
        return exerciseService.createExercise(exerciseRequest);
    }

    public ResponseEntity<String> deleteExercise(){
        return new ResponseEntity<>("deleteTest", HttpStatus.OK);
    }

    @GetMapping ("/get/{name}")
    public ResponseEntity<String> getExercise(@PathVariable String name){
        return exerciseService.getExercise(name);
    }
}
