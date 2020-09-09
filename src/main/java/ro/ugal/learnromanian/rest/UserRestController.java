package ro.ugal.learnromanian.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ro.ugal.learnromanian.repository.UserRepository;
import ro.ugal.learnromanian.request.UserAuthCheckRequest;
import ro.ugal.learnromanian.response.UserAuthCheckGenericResponse;
import ro.ugal.learnromanian.util.Message;

@RestController
@RequestMapping(path = "/user")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

//    @GetMapping(path = "/get/{userId}")
//    public UserGenericResponse getUserById(@PathVariable String userId){
//        if(userRepository.existsByUserId(Integer.valueOf(userId))){
//            return new UserGenericResponse(true, Message.userByIdFound(Integer.valueOf(userId)),
//                    userRepository.findByUserId(Integer.valueOf(userId)));
//        }else{
//            return new UserGenericResponse(false, Message.userByIdNotFound(Integer.valueOf(userId)),
//                    null);
//        }
//    }

    @PostMapping(path = "/android/check/auth")
    public UserAuthCheckGenericResponse checkUserAuthentication(
            @RequestBody UserAuthCheckRequest request){
        if (SecurityContextHolder.getContext().getAuthentication() != null
        && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            return new UserAuthCheckGenericResponse(true, Message.USER_AUTHENTICATION_TRUE,
                    userRepository.findByUserEmail(SecurityContextHolder.getContext().getAuthentication()
                            .getPrincipal().toString()));
        }else {
            return new UserAuthCheckGenericResponse(false, Message.USER_AUTHENTICATION_FALSE, null);
        }
    }

    @GetMapping(path = "/get/default")
    public UserAuthCheckRequest getBody(){
        return new UserAuthCheckRequest();
    }
}
