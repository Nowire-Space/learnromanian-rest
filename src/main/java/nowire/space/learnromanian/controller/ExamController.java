package nowire.space.learnromanian.controller;

import lombok.AllArgsConstructor;
import nowire.space.learnromanian.model.Team;
import nowire.space.learnromanian.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/exam")
public class ExamController {

    private ExamService examService;

    @PostMapping(value ={"/create/{teamName}", "/create/{teamName}/{examName}"})
    public ResponseEntity<String> createExam(@PathVariable String teamName, @PathVariable String examName){

        return examService.createExam(teamName, examName);
    }
    @PostMapping("/delete/{examName}")
    public ResponseEntity<String> deleteExam(@PathVariable String examName){
        return examService.deleteExam(examName);
    }

}
