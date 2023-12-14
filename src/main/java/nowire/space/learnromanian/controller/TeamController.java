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

    @RolesAllowed("{ADMIN, MODERATOR, PROFESSOR}")
    @PostMapping("/create")
    public ResponseEntity<String> createTeam(@RequestBody TeamRequest teamRequest){
        return teamService.createTeam(teamRequest);
    }

    @RolesAllowed("{ADMIN, MODERATOR, PROFESSOR}")
    @PostMapping("/add/{username}")
    public ResponseEntity<String> addStudent(@PathVariable String username, @RequestParam String teamName){
        return teamService.addStudent(username, teamName);
    }
    @RolesAllowed("{ADMIN, MODERATOR, PROFESSOR}")
    @DeleteMapping("/remove/{username}")
    public boolean removeStudent(@PathVariable String username){

        return true;
    }
    @RolesAllowed("{ADMIN, MODERATOR, PROFESSOR}")
    @PostMapping("/move/{username}")
    public ResponseEntity<String> move (@PathVariable String username){

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
