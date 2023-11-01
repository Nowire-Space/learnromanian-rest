package nowire.space.learnromanian.validator;
import com.mailjet.client.errors.MailjetException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.model.VerificationToken;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.repository.VerificationTokenRepository;
import nowire.space.learnromanian.request.RegistrationRequest;
import nowire.space.learnromanian.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateAccountValidator implements ConstraintValidator<CreateAccountConstraint, RegistrationRequest> {

    @Value("${registration.validation-token.expiration}")
    private int validationTokenExpirationMinutes;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final EmailService emailService;

    public void initialize(CreateAccountConstraint constraint) {

    }
    @Override
    public boolean isValid(RegistrationRequest registrationRequest, ConstraintValidatorContext context) {
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
        System.out.println(savedUser.getUserFamilyName());
        if (savedUser.getUserId() != null) {
            try {
                emailService.sendValidationEmail(savedUser.getUserEmail(), savedUser.getUserFirstName(),
                        savedUser.getUserFamilyName(), savedUser.getToken().getToken());
            } catch (MailjetException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
            return false;

    }
    }
