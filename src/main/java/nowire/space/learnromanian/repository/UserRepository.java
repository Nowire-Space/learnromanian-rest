package nowire.space.learnromanian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nowire.space.learnromanian.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserEmail(String userEmail);
    User findByUserId(Integer userId);

    boolean existsByUserEmail(String userEmail);
    boolean existsByUserId(Integer userId);
}
