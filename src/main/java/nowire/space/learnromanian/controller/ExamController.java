package nowire.space.learnromanian.controller;

import lombok.AllArgsConstructor;
import nowire.space.learnromanian.request.ExerciseScheduleExamRequest;
import nowire.space.learnromanian.request.IdExercisesRequest;
import nowire.space.learnromanian.request.ScheduleExamRequest;
import nowire.space.learnromanian.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/exam")
public class ExamController {

    private final ExamService examService;

    @PostMapping(value ={"/create/{teamName}", "/create/{teamName}/{examName}", "/create/{teamName}/{examName}"})
    public ResponseEntity<String> createExam(@PathVariable String teamName, @PathVariable String examName,
                                             @RequestBody ExerciseScheduleExamRequest exerciseScheduleExamRequest) {

        return examService.createExam(teamName, examName, exerciseScheduleExamRequest);

    }

    @PostMapping("/delete/{examName}")
    public ResponseEntity<String> deleteExam(@PathVariable String examName) {
        return examService.deleteExam(examName);
    }

    @PostMapping("/update/{examName}")
    public ResponseEntity<String> reScheduleExam(@PathVariable String examName,
                                                 @RequestBody ScheduleExamRequest scheduleExamRequest) {
        return examService.reScheduleExam(examName, scheduleExamRequest);
    }
}
