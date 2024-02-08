package nowire.space.learnromanian.service;

import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nowire.space.learnromanian.model.Exam;
import nowire.space.learnromanian.model.Team;
import nowire.space.learnromanian.repository.ExamRepository;
import nowire.space.learnromanian.repository.TeamRepository;
import nowire.space.learnromanian.util.Enum;
import nowire.space.learnromanian.util.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ExamService {

    private TeamRepository teamRepository;
    private ExamRepository examRepository;

    @Secured({Enum.Role.ADMIN, Enum.Role.MODERATOR, Enum.Role.PROFESSOR})
    public ResponseEntity<String> createExam(@Nonnull String teamName, @Nonnull String examName) {
       Team team = teamRepository.findByName(teamName);
       if(team != null) {
           Exam exam = Exam.builder().team(team).name(examName).build();
           examRepository.save(exam);
           return new ResponseEntity<>(Message.EXAM_CREATED(teamName,examName), HttpStatus.OK);
       }
       else{
           return new ResponseEntity<>(Message.EXAM_FAIL_CREATION(teamName, examName), HttpStatus.NOT_FOUND);
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
}
