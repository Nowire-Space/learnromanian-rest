package ro.ugal.learnromanian.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ugal.learnromanian.model.User;
import ro.ugal.learnromanian.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/empty")
    public User getEmptyUser() {
        return new User();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody User user) {
        return accountService.createAccount(user);
    }
}
