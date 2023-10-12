package nowire.space.learnromanian.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.repository.VerificationTokenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
@RequiredArgsConstructor
@Slf4j
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
@Component
public class ValidateAccountValidator implements ConstraintValidator<ValidateAccountConstraint, String> {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    @Getter
    private String message;

    @Override
    public void initialize(ValidateAccountConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String token, ConstraintValidatorContext context) {
            return validatedAccount(token, userRepository, verificationTokenRepository);
    }

    public boolean validatedAccount(String token, UserRepository userRepository, VerificationTokenRepository verificationTokenRepository) {
        Optional<User> userOptional = userRepository.findUserByTokenToken(token);
        if (userOptional.isEmpty()|| token == null) {
            message = ResponseEntity.notFound().build().toString();
            return false;
        }
        else {
            User user = userOptional.get();
            verificationTokenRepository.deleteById(user.getToken().getTokenId());
            user.setUserActivated(true);
            user.setToken(null);
            userRepository.save(user);
            message = ResponseEntity.ok().build().toString();
            return true;
        }
    }
}
