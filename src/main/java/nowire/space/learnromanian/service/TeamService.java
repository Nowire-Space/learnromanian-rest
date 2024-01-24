package nowire.space.learnromanian.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.model.Team;
import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.repository.TeamRepository;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.request.TeamRequest;
import nowire.space.learnromanian.util.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final UserRepository userRepository;

    private Set<User> users;

    @RolesAllowed({"ADMIN", "MODERATOR", "PROFESSOR"})
    public ResponseEntity<String> createTeam(TeamRequest teamRequest) {
            if (teamRequest != null) {
                Team newTeam = (teamRepository.findByName(teamRequest.getName()) != null) ? teamRepository.findByName(teamRequest.getName())
                        : Team.builder()
                        .name(teamRequest.getName()).description(teamRequest.getDescription())
                        .students(teamRequest.getStudents()).build();
                Team savedTeam = teamRepository.save(newTeam);
                log.info("Team created {}", savedTeam.getDescription());
                return new ResponseEntity<>(Message.TEAM_CREATED(teamRequest.getName(),teamRequest.getDescription()), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }

    }
    @RolesAllowed({"ADMIN", "MODERATOR", "PROFESSOR"})
    public ResponseEntity<String> addStudent(String username, String teamName) {
        Team team = teamRepository.findByName(teamName);
        User user = userRepository.findByUserEmail(username).get();
        if (!team.getStudents().isEmpty()) {
            users = team.getStudents();
            users.add(user);
            teamRepository.save(team);
            team.getStudents().forEach(user1 -> log.info("The team has following users {}", user1.getUserFamilyName()));
            return new ResponseEntity<>(Message.USER_ADDED_TO_THE_TEAM(username), HttpStatus.OK);
        } else if (user != null) {
            users.add(user);
            team.setStudents(users);
            teamRepository.save(team);
            team.getStudents();
            return new ResponseEntity<>(Message.USER_ADDED_TO_THE_TEAM(username), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @RolesAllowed({"ADMIN", "MODERATOR", "PROFESSOR"})
    public ResponseEntity<String> removeStudent(String username, String teamName){
        Team team = teamRepository.findByName(teamName);
        User user = userRepository.findByUserEmail(username).get();
        if (team != null && team.getStudents().remove(user)){
            teamRepository.save(team);
            return new ResponseEntity<>(Message.USER_REMOVED_FROM_THE_TEAM(username), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @RolesAllowed({"ADMIN", "MODERATOR", "PROFESSOR"})
    public ResponseEntity<String> move(String username, String actualTeamName, String newTeamName) {
       Team actualTeam = teamRepository.findByName(actualTeamName);
       Team newTeam = teamRepository.findByName(newTeamName);
       User user = userRepository.findByUserEmail(username).get();
       if (actualTeam != null && newTeam != null && actualTeam.getStudents().contains(username)){
           actualTeam.getStudents().remove(user);
           teamRepository.save(actualTeam);
           newTeam.getStudents().add(user);
           teamRepository.save(newTeam);
           return new ResponseEntity<>(Message.USER_MOVED_TO_OTHER_TEAM(username, actualTeamName, newTeamName), HttpStatus.OK);
       }
       else {
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }

    }
}

