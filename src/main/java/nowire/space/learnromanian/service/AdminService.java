package nowire.space.learnromanian.service;

import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.util.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> enableAccount(Integer userId) {
        User requestedUser = userRepository.findByUserId(userId);
        if (!requestedUser.isUserEnabled()) {
            requestedUser.setUserEnabled(true);
            User updatedUser = userRepository.save(requestedUser);
            return new ResponseEntity<>(Message.ADMIN_VALIDATION_SUCCESS(updatedUser.getUserFirstName(),
                    updatedUser.getUserFamilyName(), updatedUser.getUserEmail()), HttpStatus.OK);
        }
        return new ResponseEntity<>(Message.ADMIN_VALIDATION_ERROR, HttpStatus.BAD_REQUEST);
    }
}
