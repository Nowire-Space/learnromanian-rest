package ro.ugal.learnromanian.response;

import ro.ugal.learnromanian.model.User;

public class UserGenericResponse extends GenericResponse {
    protected User responseUser;

    public UserGenericResponse(User responseUser) {
        this.responseUser = responseUser;
    }

    public UserGenericResponse(boolean successStatus, String message, User responseUser) {
        super(successStatus, message);
        this.responseUser = responseUser;
    }

    public User getResponseUser() {
        return responseUser;
    }

    public void setResponseUser(User responseUser) {
        this.responseUser = responseUser;
    }
}
