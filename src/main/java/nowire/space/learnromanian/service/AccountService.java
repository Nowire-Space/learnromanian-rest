package nowire.space.learnromanian.service;
import com.mailjet.client.errors.MailjetException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.configuration.JwtService;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import nowire.space.learnromanian.model.User;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    @Value("${registration.validation-token.expiration}")
    private int validationTokenExpirationMinutes;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final JwtService jwtService;

    private final EmailService emailService;

    public ResponseEntity<String> createAccount(@Valid RegistrationRequest registrationRequest){
        User user =  userRepository.findByUserEmail(registrationRequest.getUserEmail()).get();
        return new ResponseEntity<String>(Message.USER_REGISTRATION_TRUE(user.getUserFirstName(),user.getUserFamilyName(),user.getUserEmail()),HttpStatus.CREATED);
    }
    public AuthenticationResponse authenticate(@Valid LoginRequest request) {
       User user = userRepository.findByUserEmail(request.getUsername()).get();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Transactional
    public ResponseEntity<String> validate(@Nonnull String token) {
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

    @RolesAllowed({"STUDENT"})
    public ResponseEntity<User> getUserProfile(String username) {
       User user =  userRepository.findByUserEmail(username).orElseThrow(()-> new UsernameNotFoundException("Username not found"));
       log.info("USER ROLE is {}", user.getRole().getRoleName() );
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RolesAllowed({"STUDENT"})
    public Page<User> getAll(int page, int rowsPerPage, String sortBy, boolean desc){
        Sort sort = desc ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, rowsPerPage, sort);
        List<User> users = userRepository.findAll();
        return new PageImpl<>(users, pageable, users.size());
    }
}
