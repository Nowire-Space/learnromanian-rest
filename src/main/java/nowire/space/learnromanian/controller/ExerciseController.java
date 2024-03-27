package nowire.space.learnromanian.controller;

import lombok.AllArgsConstructor;
import nowire.space.learnromanian.model.CompleteSentenceExercise;
import nowire.space.learnromanian.model.FreeWritingExercise;
import nowire.space.learnromanian.request.CompleteSentenceRequest;
import nowire.space.learnromanian.request.FreeWritingRequest;
import nowire.space.learnromanian.request.MultipleChoiceRequest;
import nowire.space.learnromanian.service.ExerciseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/exercise")
public class ExerciseController {

    private ExerciseService exerciseService;

    @PostMapping("/createMCE")
    public ResponseEntity<String> createMultipleChoiceExercise(@RequestBody MultipleChoiceRequest exerciseRequest){
        return exerciseService.createMultipleChoiceExercise(exerciseRequest);
    }
    @PostMapping("/createFWE")
    public ResponseEntity<String> createFreeWritingExercise(@RequestBody FreeWritingRequest exerciseRequest){
        return exerciseService.createFreeWritingExercise(exerciseRequest);
    }
    @PostMapping("/createCSE")
    public ResponseEntity<String> createCompleteSentenceExercise(@RequestBody CompleteSentenceRequest exerciseRequest){
        return exerciseService.createCompleteSentenceExercise(exerciseRequest);
    }


    public ResponseEntity<String> deleteExercise(){
        return new ResponseEntity<>("deleteTest", HttpStatus.OK);
    }

//    @GetMapping ("/get/{name}")
//    public ResponseEntity<String> getExercise(@PathVariable String name){
//        return exerciseService.getExercise(name);
//    }
}
