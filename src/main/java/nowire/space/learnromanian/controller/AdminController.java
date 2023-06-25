package nowire.space.learnromanian.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nowire.space.learnromanian.request.RegistrationRequest;
import nowire.space.learnromanian.request.UserEnableRequest;
import nowire.space.learnromanian.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/empty")
    public RegistrationRequest getEmptyUser() {
        return RegistrationRequest.builder().build();
    }

    @PostMapping("/enable")
    public ResponseEntity<String> enableAccount(@Valid @RequestBody UserEnableRequest request) {
        return adminService.enableAccount(request);
    }

    @DeleteMapping("/reject/{userId}")
    public ResponseEntity<String> rejectAccount(@PathVariable String userId) {
        return adminService.rejectAccount(userId);
    }
}
