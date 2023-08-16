package nowire.space.learnromanian.controller;

import com.mailjet.client.errors.MailjetException;
import jakarta.validation.Valid;
import nowire.space.learnromanian.request.LoginRequest;
import nowire.space.learnromanian.request.PasswordResetRequest;
import nowire.space.learnromanian.request.RegistrationRequest;
import nowire.space.learnromanian.response.AuthenticationResponse;
import nowire.space.learnromanian.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

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
}
