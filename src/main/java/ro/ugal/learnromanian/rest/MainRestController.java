package ro.ugal.learnromanian.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ro.ugal.learnromanian.model.Role;
import ro.ugal.learnromanian.model.User;
import ro.ugal.learnromanian.repository.RoleRepository;
import ro.ugal.learnromanian.repository.UserRepository;
import ro.ugal.learnromanian.request.RegistrationRequest;
import ro.ugal.learnromanian.response.GenericResponse;
import ro.ugal.learnromanian.response.LoginResponse;
import ro.ugal.learnromanian.util.Enum;
import ro.ugal.learnromanian.util.Message;

@RestController
public class MainRestController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping(path = "/login")
    public LoginResponse login() {
        if (SecurityContextHolder.getContext().getAuthentication().getName() != null) {
            return new LoginResponse(true, Message.USER_LOGIN_TRUE, userRepository.findByUserEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()));
        }else {
            return new LoginResponse(false, Message.USER_LOGIN_FALSE, null);
        }
    }

    @PostMapping(path = "/register")
    public GenericResponse register(@RequestBody RegistrationRequest request) {
        request.toString();
        if(!userRepository.existsByUserEmail(request.getEmail())) {
            Role studentRole = roleRepository.getByRoleName(Enum.DBRole.STUDENT);
            User newUser = userRepository.save(new User(request.getName(), request.getFamilyName(),
                    encoder.encode(request.getPassword()), request.getPhoneNumber(), request.getEmail(), studentRole));
            if (newUser.getUserId() > 0) {
                return new GenericResponse(true, Message.USER_REGISTRATION_TRUE);
            }else {
                return new GenericResponse(false, Message.USER_REGISTRATION_ERROR);
            }
        }else {
            return new GenericResponse(false, Message.USER_REGISTRATION_FALSE);
        }
    }

    @GetMapping("/gettt")
    public RegistrationRequest test() {
        return new RegistrationRequest("aaa", "aaa", "aaa", "aaa", "aaa");
    }
}
