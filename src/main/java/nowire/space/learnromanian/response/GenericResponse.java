package nowire.space.learnromanian.response;

public class GenericResponse {

    protected boolean successStatus;
    protected String message;

    public GenericResponse() {
    }

    public GenericResponse(boolean successStatus, String message) {
        this.successStatus = successStatus;
        this.message = message;
    }

    public boolean isSuccessStatus() {
        return successStatus;
    }

    public void setSuccessStatus(boolean successStatus) {
        this.successStatus = successStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
