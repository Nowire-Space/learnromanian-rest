package ro.ugal.learnromanian.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ro.ugal.learnromanian.model.Role;
import ro.ugal.learnromanian.model.User;
import ro.ugal.learnromanian.repository.RoleRepository;
import ro.ugal.learnromanian.repository.UserRepository;
import ro.ugal.learnromanian.request.LoginRequest;
import ro.ugal.learnromanian.request.RegistrationRequest;
import ro.ugal.learnromanian.response.JwtResponse;
import ro.ugal.learnromanian.response.MessageResponse;
import ro.ugal.learnromanian.security.jwt.JwtUtils;
import ro.ugal.learnromanian.security.services.UserDetailsImpl;
import ro.ugal.learnromanian.util.Enum;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                Long.valueOf(userDetails.getId()),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getAuthorities().iterator().next().getAuthority()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {

        if (userRepository.existsByUserEmail(registrationRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Role studentRole = roleRepository.getByRoleName(Enum.Role.STUDENT);
        User user = new User(registrationRequest.getName(),
                registrationRequest.getFamilyName(),
                encoder.encode(registrationRequest.getPassword()),
                registrationRequest.getPhoneNumber(),
                registrationRequest.getEmail(),
                studentRole);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
