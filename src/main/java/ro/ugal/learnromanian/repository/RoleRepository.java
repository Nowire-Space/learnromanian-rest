package ro.ugal.learnromanian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.ugal.learnromanian.model.Role;
import ro.ugal.learnromanian.model.User;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
