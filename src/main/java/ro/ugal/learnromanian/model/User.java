package ro.ugal.learnromanian.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    protected Integer userId;

    @Column(name="user_name")
    protected String userName;

    @Column(name="user_family_name")
    protected String userFamilyName;

    @JsonIgnore
    @Column(name="user_password")
    protected String userPassword;

    @Column(name="user_phone_number")
    protected String userPhoneNumber;

    @Column(name="user_email")
    protected String userEmail;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_role_id")
    protected Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_photo_id")
    protected UserPhoto photo;

    public User() {
    }

    public User(String userName, String userFamilyName, String userPassword, String userPhoneNumber, String userEmail,
                Role role, UserPhoto photo) {
        this.userName = userName;
        this.userFamilyName = userFamilyName;
        this.userPassword = userPassword;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.role = role;
        this.photo = photo;
    }

    public User(Integer userId, String userName, String userFamilyName, String userPassword, String userPhoneNumber,
                String userEmail, Role role, UserPhoto photo) {
        this.userId = userId;
        this.userName = userName;
        this.userFamilyName = userFamilyName;
        this.userPassword = userPassword;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.role = role;
        this.photo = photo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFamilyName() {
        return userFamilyName;
    }

    public void setUserFamilyName(String userFamilyName) {
        this.userFamilyName = userFamilyName;
    }

    @JsonIgnore
    public String getUserPassword() {
        return userPassword;
    }

    @JsonProperty
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(UserPhoto photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("userId=").append(userId);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", userFamilyName='").append(userFamilyName).append('\'');
        sb.append(", userPassword='").append(userPassword).append('\'');
        sb.append(", userPhoneNumber='").append(userPhoneNumber).append('\'');
        sb.append(", userEmail='").append(userEmail).append('\'');
        sb.append(", role=").append(role);
        sb.append(", photo=").append(photo);
        sb.append('}');
        return sb.toString();
    }
}
