package ro.ugal.learnromanian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.ugal.learnromanian.repository.UserRepository;
import ro.ugal.learnromanian.response.LoginResponse;
import ro.ugal.learnromanian.util.Message;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/login")
    public LoginResponse login() {
        if (SecurityContextHolder.getContext().getAuthentication().getName() != null) {
            return new LoginResponse(true, Message.USER_LOGIN_TRUE, userRepository.findByUserEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()));
        }else {
            return new LoginResponse(false, Message.USER_LOGIN_FALSE, null);
        }
    }
}
