package ro.ugal.learnromanian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.ugal.learnromanian.model.User;
import ro.ugal.learnromanian.repository.UserRepository;
import ro.ugal.learnromanian.response.UserGenericResponse;
import ro.ugal.learnromanian.util.Message;

@RestController
@RequestMapping(path = "/user")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/get/{userId}")
    public UserGenericResponse getUserById(@PathVariable String userId){
        if(userRepository.existsByUserId(Integer.valueOf(userId))){
            return new UserGenericResponse(true, Message.userByIdFound(Integer.valueOf(userId)),
                    userRepository.findByUserId(Integer.valueOf(userId)));
        }else{
            return new UserGenericResponse(false, Message.userByIdNotFound(Integer.valueOf(userId)),
                    null);
        }
    }
}
