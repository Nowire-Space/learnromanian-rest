package nowire.space.learnromanian.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import nowire.space.learnromanian.request.TeamRequest;
import nowire.space.learnromanian.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/team")
public class TeamController {

    private TeamService teamService;

    @PostMapping("/create")
    public ResponseEntity<String> createTeam(@RequestBody TeamRequest teamRequest){
        return teamService.createTeam(teamRequest);
    }


    @PostMapping(value = {"/addStudent/{username}", "/addStudent/{username}/{teamName}"})
    public ResponseEntity<String> addStudent(@PathVariable String username, @PathVariable String teamName){
        return teamService.addStudent(username, teamName);
    }

    @DeleteMapping(value = {"/remove/{username}","/remove/{username}/{teamName}"})
    public ResponseEntity<String> removeStudent(@PathVariable String username, @PathVariable String teamName){
        return teamService.removeStudent(username, teamName);
    }

    @PostMapping(value = {"/move/{username}", "/move/{username}/{actualTeamName}", "/move/{username}/{actualTeamName}/{newTeamName}"})
    public ResponseEntity<String> move (@PathVariable String username, String actualTeamName, String newTeamName){
        return teamService.move(username,actualTeamName,newTeamName);
    }


}
