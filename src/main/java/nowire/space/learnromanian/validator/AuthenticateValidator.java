package nowire.space.learnromanian.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.configuration.JwtService;
import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.request.LoginRequest;
import nowire.space.learnromanian.response.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticateValidator implements ConstraintValidator<AuthenticateConstraint, LoginRequest> {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JwtService jwtService;
    @Getter
    private String jwtToken;

    @Override
    public void initialize(AuthenticateConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LoginRequest request, ConstraintValidatorContext context) {

        if(request != null){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword()));
        User user = userRepository.findByUserEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        jwtToken = jwtService.generateToken(user);
        AuthenticationResponse.builder().token(jwtToken).build();

        return true;
    }
        return false;
    }

}


