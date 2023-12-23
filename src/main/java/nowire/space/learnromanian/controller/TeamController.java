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


//    @PostMapping("/addStudent/{username}/{teamName}")
    @PostMapping(value = {"/addStudent/{username}", "/addStudent/{username}/{teamName}"})
    public ResponseEntity<String> addStudent(@PathVariable String username, @PathVariable String teamName){
        return teamService.addStudent(username, teamName);
    }

    @DeleteMapping("/remove/{username}")
    public boolean removeStudent(@PathVariable String username){
        return true;
    }

    @PostMapping("/move/{username}")
    public ResponseEntity<String> move (@PathVariable String username){

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
