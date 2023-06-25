package nowire.space.learnromanian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nowire.space.learnromanian.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(String roleName);

    Role findByRoleId(Integer roleId);
}
