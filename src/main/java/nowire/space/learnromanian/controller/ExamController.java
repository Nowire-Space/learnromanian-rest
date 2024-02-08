package nowire.space.learnromanian.controller;

import lombok.AllArgsConstructor;
import nowire.space.learnromanian.model.Team;
import nowire.space.learnromanian.request.ScheduleExamRequest;
import nowire.space.learnromanian.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/exam")
public class ExamController {

    private ExamService examService;

    @PostMapping(value ={"/create/{teamName}", "/create/{teamName}/{examName}"})
    public ResponseEntity<String> createExam(@PathVariable String teamName, @PathVariable String examName, @RequestBody ScheduleExamRequest scheduleExamRequest){

        return examService.createExam(teamName, examName, scheduleExamRequest);
    }
    @PostMapping("/delete/{examName}")
    public ResponseEntity<String> deleteExam(@PathVariable String examName){
        return examService.deleteExam(examName);
    }

    @PostMapping("/update/{examName}")
    public ResponseEntity<String> reScheduleExam(@PathVariable String examName, @RequestBody ScheduleExamRequest scheduleExamRequest){
        return examService.reScheduleExam(examName, scheduleExamRequest);
    }

}
