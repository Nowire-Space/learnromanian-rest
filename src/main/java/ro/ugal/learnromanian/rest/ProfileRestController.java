package ro.ugal.learnromanian.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ro.ugal.learnromanian.repository.UserRepository;
import ro.ugal.learnromanian.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/profile")
public class ProfileRestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils tokenUtils;

    @GetMapping("/get")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        return ResponseEntity.ok(userRepository.findByUserEmail(authentication.getName()));
    }
}