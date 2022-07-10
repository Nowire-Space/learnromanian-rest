package ro.ugal.learnromanian.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.ugal.learnromanian.service.RoleService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/role")
public class RoleRestController {

    private final RoleService roleService;

    public RoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAllAvailableRolesForRegistration() {
        return new ResponseEntity<>(roleService.getAllAvailableRolesForRegistration(), HttpStatus.OK);
    }
}
