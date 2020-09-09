package ro.ugal.learnromanian.response;

import ro.ugal.learnromanian.model.User;

public class UserAuthCheckGenericResponse extends GenericResponse {

    protected User user;

    public UserAuthCheckGenericResponse(User user) {
        this.user = user;
    }

    public UserAuthCheckGenericResponse(boolean successStatus, String message, User user) {
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
