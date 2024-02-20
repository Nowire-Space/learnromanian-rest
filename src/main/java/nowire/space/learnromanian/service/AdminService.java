package nowire.space.learnromanian.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nowire.space.learnromanian.model.Role;
import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.repository.RoleRepository;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.request.UserEnableRequest;
import nowire.space.learnromanian.util.Enum;
import nowire.space.learnromanian.util.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Secured({Enum.Role.ADMIN, Enum.Role.MODERATOR, Enum.Role.PROFESSOR})
    public ResponseEntity<String> enableAccount(UserEnableRequest request) {
        Optional<User> requestedUser = userRepository.findByUserId(request.getUserId());
        if (requestedUser.isPresent()) {
            if (!requestedUser.get().isUserEnabled() && requestedUser.get().isUserActivated()) {
                Role requestedRole = roleRepository.getReferenceById(request.getRoleId());
                requestedUser.get().setUserEnabled(true);
                requestedUser.get().setRole(requestedRole);
                User updatedUser = userRepository.save(requestedUser.get());
                return new ResponseEntity<>(Message.ADMIN_VALIDATION_TRUE(updatedUser.getUserFirstName(),
                        updatedUser.getUserFamilyName(), updatedUser.getUserEmail()), HttpStatus.OK);
            }
            return new ResponseEntity<>(Message.USER_NOT_ACTIVATED(request.getUserId()), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(Message.WRONG_EMAIL_ADDRESS,
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Secured({Enum.Role.ADMIN, Enum.Role.MODERATOR, Enum.Role.PROFESSOR})
    @Transactional
    public ResponseEntity<String> rejectAccount(String userId) {
        Integer deletedCount = userRepository.deleteByUserId(Integer.valueOf(userId));
        if (deletedCount.equals(1)) {
            return new ResponseEntity<>(Message.ADMIN_REJECT_TRUE(userId), HttpStatus.OK);
        }
        return new ResponseEntity<>(Message.ADMIN_REJECT_FALSE(userId), HttpStatus.BAD_REQUEST);
    }
}
