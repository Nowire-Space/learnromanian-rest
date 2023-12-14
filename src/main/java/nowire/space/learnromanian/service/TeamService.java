package nowire.space.learnromanian.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.model.Team;
import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.repository.TeamRepository;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.request.TeamRequest;
import nowire.space.learnromanian.util.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class TeamService {

    private TeamRepository teamRepository;
    private UserRepository userRepository;

    public ResponseEntity<String> createTeam(TeamRequest teamRequest) {
            if (teamRequest != null) {
                Team newTeam = (teamRepository.findByName(teamRequest.getName()) != null) ? teamRepository.findByName(teamRequest.getName())
                        :Team.builder().name(teamRequest.getName()).description(teamRequest.getDescription()).users(teamRequest.getStudents()).build();
                Team savedTeam = teamRepository.save(newTeam);
                log.info(savedTeam.getUsers().toString());
                return new ResponseEntity<>(Message.TEAM_CREATED(teamRequest.getName(),teamRequest.getDescription()), HttpStatus.OK);
            }

            else {

                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }

    }

    public ResponseEntity<String> addStudent(String username, String teamName) {

        Team team = teamRepository.findByName(teamName);
        team.getUsers().add(userRepository.findByUserEmail(username).get());
        teamRepository.save(team);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


}

