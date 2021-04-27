package ro.ugal.learnromanian.response;

import ro.ugal.learnromanian.model.User;

public class LoginResponse extends GenericResponse {

    private User user;

    public LoginResponse(User user) { }

    public LoginResponse(boolean successStatus, String message, User user) {
        super(successStatus, message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
