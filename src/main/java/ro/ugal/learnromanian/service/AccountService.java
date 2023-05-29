package ro.ugal.learnromanian.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.ugal.learnromanian.model.User;
import ro.ugal.learnromanian.repository.UserRepository;

@Service
public class AccountService {

    private final UserRepository userRepository;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> createAccount(User user) {
        User savedUser = userRepository.save(user);
        if (savedUser.getUserId() != null) {
            return new ResponseEntity<>("New user with email {} and username {} was created.", HttpStatus.OK);
        }
        return new ResponseEntity<>("User was not created.", HttpStatus.BAD_REQUEST);
    }
}
