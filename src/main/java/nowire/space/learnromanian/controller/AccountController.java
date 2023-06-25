package nowire.space.learnromanian.controller;

import jakarta.validation.Valid;
import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.request.LoginRequest;
import nowire.space.learnromanian.request.RegistrationRequest;
import nowire.space.learnromanian.response.AuthenticationResponse;
import nowire.space.learnromanian.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/empty")
    public User getEmptyUser() {
        return accountService.getUser();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return accountService.createAccount(registrationRequest);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(accountService.authenticate(request));
    }
}
