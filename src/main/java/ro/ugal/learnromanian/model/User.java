package ro.ugal.learnromanian.model;

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

    @Column(name="user_password")
    protected String userPassword;

    @Column(name="user_phone_number")
    protected String userPhoneNumber;

    @Column(name="user_email")
    protected String userEmail;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_role_id")
    protected Role role;

    public User() {
    }

    public User(String userName, String userFamilyName, String userPassword, String userPhoneNumber, String userEmail, Role role) {
        this.userName = userName;
        this.userFamilyName = userFamilyName;
        this.userPassword = userPassword;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.role = role;
    }

    public User(Integer userId, String userName, String userFamilyName, String userPassword, String userPhoneNumber, String userEmail, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.userFamilyName = userFamilyName;
        this.userPassword = userPassword;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.role = role;
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

    public String getUserPassword() {
        return userPassword;
    }

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
        sb.append('}');
        return sb.toString();
    }
}
