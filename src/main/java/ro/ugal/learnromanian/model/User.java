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

    public User() {
    }

    public User(String userName, String userFamilyName, String userPassword, String userPhoneNumber, String userEmail) {
        this.userName = userName;
        this.userFamilyName = userFamilyName;
        this.userPassword = userPassword;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
    }

    public User(Integer userId, String userName, String userFamilyName, String userPassword, String userPhoneNumber, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userFamilyName = userFamilyName;
        this.userPassword = userPassword;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
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

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userFamilyName='" + userFamilyName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
