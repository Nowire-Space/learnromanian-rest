package ro.ugal.learnromanian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.ugal.learnromanian.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserEmail(String userEmail);
    boolean existsByUserEmail(String userEmail);
    User findByUserId(Integer userId);
    boolean existsByUserId(Integer userId);
}
