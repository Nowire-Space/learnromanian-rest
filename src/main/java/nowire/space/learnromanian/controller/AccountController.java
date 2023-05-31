package nowire.space.learnromanian.controller;

import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.service.AccountService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> getEmptyUser() {
        return new ResponseEntity<>("Hello from the other side!", HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody User user) {
        return accountService.createAccount(user);
    }
}
