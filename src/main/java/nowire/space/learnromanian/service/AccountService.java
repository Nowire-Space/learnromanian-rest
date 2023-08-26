package nowire.space.learnromanian.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mailjet.client.errors.MailjetException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.configuration.JwtService;
import nowire.space.learnromanian.model.VerificationToken;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.repository.VerificationTokenRepository;
import nowire.space.learnromanian.request.LoginRequest;
import nowire.space.learnromanian.request.PasswordResetRequest;
import nowire.space.learnromanian.request.RegistrationRequest;
import nowire.space.learnromanian.response.AuthenticationResponse;
import nowire.space.learnromanian.util.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import nowire.space.learnromanian.model.User;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public ResponseEntity<String> resetPassword(PasswordResetRequest request) throws MailjetException {
        Optional<User> userOptional = userRepository.findByUserEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String newPassword = generateNewPassword();
        User user = userOptional.get();
        emailService.sendPasswordResetEmail(user.getUserEmail(), newPassword);
        user.setUserPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok(Message.PASSWORD_RESET_TRUE);
    }

    private String generateNewPassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 8;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
    @PreAuthorize("hasRole('STUDENT')")
    public String getUserProfile(String username) throws CloneNotSupportedException {
       User user =  userRepository.findByUserEmail(username).orElseThrow(()-> new UsernameNotFoundException("Username not found"));
       User userClone = (User)user.clone();
       userClone.setUserPassword(null);
       userClone.setPhoto(null);
       ObjectMapper objectMapper = new ObjectMapper();
       String jsonString;
       try{
           jsonString = objectMapper.writeValueAsString(userClone);
       } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
       }
        return jsonString;
    }
    @PreAuthorize("hasRole('STUDENT')")
    public Page<User> getAll(int page, int rowsPerPage, String sortBy, boolean desc){
        Sort sort = desc ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, rowsPerPage, sort);
        List<User> users = userRepository.findAll();
        return new PageImpl<>(users, pageable, users.size());
    }
}
