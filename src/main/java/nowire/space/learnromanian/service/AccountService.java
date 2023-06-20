package nowire.space.learnromanian.service;

import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.request.RegistrationRequest;
import nowire.space.learnromanian.util.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import nowire.space.learnromanian.model.User;

@Service
public class AccountService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public AccountService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> createAccount(RegistrationRequest registrationRequest) {
        User newUser = User.builder()
                .userFirstName(registrationRequest.getUserFirstName())
                .userFamilyName(registrationRequest.getUserFamilyName())
                .userEmail(registrationRequest.getUserEmail())
                .userPhoneNumber(registrationRequest.getUserPhoneNumber())
                .userPassword(passwordEncoder.encode(registrationRequest.getUserPassword()))
                .userEnabled(false)
                .build();
        User savedUser = userRepository.save(newUser);
        if (savedUser.getUserId() != null) {
            return new ResponseEntity<>(Message.USER_REGISTRATION_TRUE(savedUser.getUserFirstName(),
                    savedUser.getUserFamilyName(), savedUser.getUserEmail()), HttpStatus.OK);
        }
        return new ResponseEntity<>(Message.USER_REGISTRATION_ERROR, HttpStatus.BAD_REQUEST);
    }
}
