package ro.ugal.learnromanian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.ugal.learnromanian.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserEmail(String userEmail);
    User findByUserId(Integer userId);

    boolean existsByUserEmail(String userEmail);
    boolean existsByUserId(Integer userId);
}
