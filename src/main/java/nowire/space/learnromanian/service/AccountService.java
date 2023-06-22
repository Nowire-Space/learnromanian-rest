package nowire.space.learnromanian.service;

import lombok.AllArgsConstructor;
import nowire.space.learnromanian.configuration.JwtService;
import nowire.space.learnromanian.model.Role;
import nowire.space.learnromanian.repository.RoleRepository;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.request.LoginRequest;
import nowire.space.learnromanian.request.RegistrationRequest;
import nowire.space.learnromanian.response.AuthenticationResponse;
import nowire.space.learnromanian.util.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import nowire.space.learnromanian.model.User;

import java.util.Collections;

@Service
@AllArgsConstructor
public class AccountService {

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    public User getUser() {
        return userRepository.findByUserEmail("bogdan.dabija@yahoo.com").get();
    }

    public ResponseEntity<String> createAccount(RegistrationRequest registrationRequest) {
        Role adminRole = roleRepository.getReferenceById(1);
        User newUser = User.builder()//TODO for testing purposes
                .userFirstName(registrationRequest.getUserFirstName())
                .userFamilyName(registrationRequest.getUserFamilyName())
                .userEmail(registrationRequest.getUserEmail())
                .userPhoneNumber(registrationRequest.getUserPhoneNumber())
                .userPassword(passwordEncoder.encode(registrationRequest.getUserPassword()))
                .userEnabled(false)
                .role(adminRole)//TODO for testing purposes
                .build();

        User savedUser = userRepository.save(newUser);
        if (savedUser.getUserId() != null) {
            return new ResponseEntity<>(Message.USER_REGISTRATION_TRUE(savedUser.getUserFirstName(),
                    savedUser.getUserFamilyName(), savedUser.getUserEmail()), HttpStatus.OK);
        }
        return new ResponseEntity<>(Message.USER_REGISTRATION_ERROR, HttpStatus.BAD_REQUEST);
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword()));
        User user = userRepository.findByUserEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }
}
