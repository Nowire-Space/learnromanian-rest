package ro.ugal.learnromanian.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ro.ugal.learnromanian.model.Role;
import ro.ugal.learnromanian.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {

    private Integer id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Role role;

    public UserDetailsImpl(Integer id, String username, String email, String password, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getUserId(),
                user.getUserFamilyName().concat(" ").concat(user.getUserName()),
                user.getUserEmail(),
                user.getUserPassword(),
                user.getRole());
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> authorities = new ArrayList<>();
        authorities.add(role);
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) obj;
        return Objects.equals(id, user.id);
    }
}