package nowire.space.learnromanian.service;

import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import nowire.space.learnromanian.model.Exam;
import nowire.space.learnromanian.model.Team;
import nowire.space.learnromanian.repository.ExamRepository;
import nowire.space.learnromanian.repository.TeamRepository;
import nowire.space.learnromanian.request.ScheduleExamRequest;
import nowire.space.learnromanian.util.Enum;
import nowire.space.learnromanian.util.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExamService {

    private TeamRepository teamRepository;
    private ExamRepository examRepository;

    @Secured({Enum.Role.ADMIN, Enum.Role.MODERATOR, Enum.Role.PROFESSOR})
    public ResponseEntity<String> createExam(@Nonnull String teamName, @Nonnull String examName, ScheduleExamRequest scheduleExamRequest) {
       Team team = teamRepository.findByName(teamName);
       if(team != null) {
           Exam exam = Exam.builder().team(team).name(examName).scheduleExam(LocalDateTime.of(scheduleExamRequest.getYear()
                   ,scheduleExamRequest.getMonth(),scheduleExamRequest.getDayOfMonth(), scheduleExamRequest.getHour(), scheduleExamRequest.getMinute()
           ,0,0)).build();
           examRepository.save(exam);
           return new ResponseEntity<>(Message.EXAM_CREATED(examName, teamName), HttpStatus.OK);
       }
       else{
           return new ResponseEntity<>(Message.EXAM_FAIL_CREATION(examName, teamName), HttpStatus.NOT_FOUND);
       }
    }

    @Transactional
    @Secured({Enum.Role.ADMIN, Enum.Role.MODERATOR, Enum.Role.PROFESSOR})
    public ResponseEntity<String> deleteExam(String examName) {
        Exam exam = examRepository.findByName(examName);
        if (exam!=null){
            examRepository.deleteByName(examName);
            return new ResponseEntity<>(Message.EXAM_DELETED(examName), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(Message.EXAM_FAIL_DELETE(examName),HttpStatus.NOT_FOUND);
        }
    }
    @Secured({Enum.Role.ADMIN, Enum.Role.MODERATOR, Enum.Role.PROFESSOR})
    public ResponseEntity<String> reScheduleExam(String examName, ScheduleExamRequest scheduleExamRequest){
        Exam exam = examRepository.findByName(examName);
        if(exam!=null){
            exam.setScheduleExam(LocalDateTime.of(scheduleExamRequest.getYear(),scheduleExamRequest.getMonth()
            , scheduleExamRequest.getDayOfMonth(), scheduleExamRequest.getHour(),
                    scheduleExamRequest.getMinute(),0,0));
            examRepository.save(exam);
            LocalDateTime newDate = examRepository.findByName(examName).getScheduleExam();
            return new ResponseEntity<>("Rescheduled exam on "+ newDate.toString(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Exam not found", HttpStatus.NOT_FOUND);
        }
    }


}
