package nowire.space.learnromanian.controller;

import com.mailjet.client.errors.MailjetException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.request.LoginRequest;
import nowire.space.learnromanian.request.PasswordResetRequest;
import nowire.space.learnromanian.request.RegistrationRequest;
import nowire.space.learnromanian.response.AuthenticationResponse;
import nowire.space.learnromanian.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@CrossOrigin(origins = "${webapp.url}")
@AllArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;


    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@Valid @RequestBody RegistrationRequest registrationRequest)
            throws MailjetException {
        return accountService.createAccount(registrationRequest);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(accountService.authenticate(request));
    }

    @PostMapping("/validate/{token}")
    public ResponseEntity<String> validateAccount(@PathVariable String token) {
        return accountService.validate(token);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetRequest request)
            throws MailjetException {
        return accountService.resetPassword(request);
    }
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserProfile(@PathVariable String username){
        return accountService.getUserProfile(username);

    }
    @GetMapping
    public Page<User> get(@RequestParam int page, @RequestParam int rowsPerPage, @RequestParam(required = false, defaultValue = "userFamilyName") String sortBy, @RequestParam(required = false, defaultValue = "false") boolean desc){
        return accountService.getAll(page, rowsPerPage, sortBy, desc);
    }
}
