package nowire.space.learnromanian.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nowire.space.learnromanian.model.Role;
import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.repository.RoleRepository;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.request.UserEnableRequest;
import nowire.space.learnromanian.util.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public ResponseEntity<String> enableAccount(UserEnableRequest request) {
        Optional<User> requestedUser = userRepository.findByUserId(request.getUserId());
        String regexPattern = "^(.+)@(\\S+)$";
        Pattern pat = Pattern.compile(regexPattern);
        if (requestedUser.isPresent() && pat.matcher(requestedUser.get().getUserEmail()).matches()){
            if (!requestedUser.get().isUserEnabled() && requestedUser.get().isUserActivated()) {
                Role requestedRole = roleRepository.getReferenceById(request.getRoleId());
                requestedUser.get().setUserEnabled(true);
                requestedUser.get().setRole(requestedRole);
                User updatedUser = userRepository.save(requestedUser.get());
                return new ResponseEntity<>(Message.ADMIN_VALIDATION_TRUE(updatedUser.getUserFirstName(),
                        updatedUser.getUserFamilyName(), updatedUser.getUserEmail()), HttpStatus.OK);
            }
            return new ResponseEntity<>(Message.USER_NOT_ACTIVATED(request.getUserId()), HttpStatus.valueOf(400));
        } else {
            return new ResponseEntity<>(Message.WRONG_EMAIL_ADDRESS,
                    HttpStatus.BAD_REQUEST);

        }
    }

    @Transactional
    public ResponseEntity<String> rejectAccount(String userId) {
        Integer deletedCount = userRepository.deleteByUserId(Integer.valueOf(userId));
        if (deletedCount.equals(1)) {
            return new ResponseEntity<>(Message.ADMIN_REJECT_TRUE(userId), HttpStatus.OK);
        }
        return new ResponseEntity<>(Message.ADMIN_REJECT_FALSE(userId), HttpStatus.BAD_REQUEST);
    }
}
