package nowire.space.learnromanian.service;

import com.mailjet.client.errors.MailjetException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.configuration.JwtService;
import nowire.space.learnromanian.model.VerificationToken;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.repository.VerificationTokenRepository;
import nowire.space.learnromanian.request.LoginRequest;
import nowire.space.learnromanian.request.RegistrationRequest;
import nowire.space.learnromanian.response.AuthenticationResponse;
import nowire.space.learnromanian.util.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import nowire.space.learnromanian.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    @Value("${registration.validation-token.expiration}")
    private int validationTokenExpirationMinutes;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final JwtService jwtService;

    private final EmailService emailService;

    public ResponseEntity<String> createAccount(RegistrationRequest registrationRequest) throws MailjetException {
        VerificationToken token = VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .expiration(Date.from(LocalDateTime.now()
                        .plusMinutes(validationTokenExpirationMinutes)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        User newUser = User.builder()
                .userFirstName(registrationRequest.getUserFirstName())
                .userFamilyName(registrationRequest.getUserFamilyName())
                .userEmail(registrationRequest.getUserEmail())
                .userPhoneNumber(registrationRequest.getUserPhoneNumber())
                .userPassword(passwordEncoder.encode(registrationRequest.getUserPassword()))
                .token(token)
                .userEnabled(false)
                .userActivated(false)
                .build();

        User savedUser = userRepository.save(newUser);
        if (savedUser.getUserId() != null) {
            emailService.sendValidationEmail(savedUser.getUserEmail(), savedUser.getUserFirstName(),
                    savedUser.getUserFamilyName(), savedUser.getToken().getToken());
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

    @Transactional
    public ResponseEntity<String> validate(String token) {
        Optional<User> userOptional = userRepository.findUserByTokenToken(token);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        verificationTokenRepository.deleteById(user.getToken().getTokenId());
        user.setUserActivated(true);
        user.setToken(null);
        userRepository.save(user);
        return ResponseEntity.ok(Message.USER_ACTIVATION_TRUE);
    }
}
