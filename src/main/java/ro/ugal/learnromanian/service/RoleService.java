package ro.ugal.learnromanian.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ro.ugal.learnromanian.model.Role;
import ro.ugal.learnromanian.repository.RoleRepository;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ro.ugal.learnromanian.util.Enum.DBRole.ADMIN;
import static ro.ugal.learnromanian.util.Enum.DBRole.MODERATOR;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllAvailableRolesForRegistration() {
        Predicate<Role> noAdminRolePredicate = role -> !role.getRoleName().equals(ADMIN);
        Predicate<Role> noModeratorRolePredicate = role -> !role.getRoleName().equals(MODERATOR);
        return roleRepository.findAll().stream().filter(noAdminRolePredicate).filter(noModeratorRolePredicate)
                .map(role -> {
                    role.setRoleName(StringUtils.capitalize(role.getRoleName().replace("ROLE_", "")
                            .toLowerCase()));
                    return role;
                }).collect(Collectors.toList());
    }
}
