package ro.ugal.learnromanian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.ugal.learnromanian.model.User;
import ro.ugal.learnromanian.repository.UserRepository;

import java.util.Arrays;

//@Service
public class ApplicationAuthenticationProvider {

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder encoder;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) {
//        String username = authentication.getName();
//        String password = authentication.getCredentials().toString();
//
//        User user = userRepository.findByUserEmail(username);
//        if(user != null && encoder.matches(password, user.getUserPassword())) {
//            return new UsernamePasswordAuthenticationToken(username, password,
//                    Arrays.asList(new SimpleGrantedAuthority(user.getRole().getRoleName())));
//        } else {
//            throw new BadCredentialsException("Please check your credentials.");
//        }
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
}
