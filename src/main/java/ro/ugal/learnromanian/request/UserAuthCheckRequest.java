package ro.ugal.learnromanian.request;

public class UserAuthCheckRequest {
    protected String userEmail;

    public UserAuthCheckRequest() {
    }

    public UserAuthCheckRequest(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
