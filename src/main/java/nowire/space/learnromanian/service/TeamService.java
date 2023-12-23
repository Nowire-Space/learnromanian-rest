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

    private TeamRepository teamRepository;
    private UserRepository userRepository;


    @RolesAllowed({"ADMIN", "MODERATOR", "PROFESSOR"})
    public ResponseEntity<String> createTeam(TeamRequest teamRequest) {
            if (teamRequest != null) {
                Team newTeam = (teamRepository.findByName(teamRequest.getName()) != null) ? teamRepository.findByName(teamRequest.getName())
                        :Team.builder().name(teamRequest.getName()).description(teamRequest.getDescription()).users(teamRequest.getStudents()).build();
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
        if (!team.getUsers().isEmpty() && user != null) {
            Set<User> users = team.getUsers();
            users.add(user);
            team.getUsers().forEach(user1 -> user1.getUserFamilyName());
            teamRepository.save(team);
            return new ResponseEntity<>(Message.USER_ADDED_TO_THE_TEAM(username), HttpStatus.OK);
        } else if (user != null) {
            Set<User> users = new HashSet<>();
            users.add(user);
            team.setUsers(users);
            teamRepository.save(team);
            return new ResponseEntity<>(Message.USER_ADDED_TO_THE_TEAM(username), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

