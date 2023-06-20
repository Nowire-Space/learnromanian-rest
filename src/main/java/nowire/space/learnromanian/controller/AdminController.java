package nowire.space.learnromanian.controller;

import nowire.space.learnromanian.request.RegistrationRequest;
import nowire.space.learnromanian.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/empty")
    public RegistrationRequest getEmptyUser() {
        return RegistrationRequest.builder().build();
    }

    @PostMapping("/enable/{userId}")
    public ResponseEntity<String> enableAccount(@PathVariable("userId") Integer userId) {
        return adminService.enableAccount(userId);
    }
}
