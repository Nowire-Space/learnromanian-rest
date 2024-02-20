package nowire.space.learnromanian.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="user")
//TO DO password should not be returned
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    protected Integer userId;

    @Column(name="user_last_name")
    protected String userFirstName;

    @Column(name="user_family_name")
    protected String userFamilyName;

    @Column(name="user_password")
    protected String userPassword;

    @Column(name="user_phone_number")
    protected String userPhoneNumber;

    @Column(name="user_email", unique = true)
    protected String userEmail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_role_id")
    protected Role role;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_photo_id")
    protected UserPhoto photo;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_token_id")
    protected VerificationToken token;

    @Column(name="user_enabled")
    protected boolean userEnabled;

    @Column(name="user_activated")
    protected boolean userActivated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    protected Team team;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userEmail;
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
        return userEnabled && userActivated;
    }

}

