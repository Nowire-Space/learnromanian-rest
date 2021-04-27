package ro.ugal.learnromanian.request;

public class RegistrationRequest {

    private String name;

    private String familyName;

    private String phoneNumber;

    private String email;

    private String password;

    public RegistrationRequest() { }

    public RegistrationRequest(String name, String familyName, String phoneNumber, String email, String password) {
        this.name = name;
        this.familyName = familyName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RegistrationRequest{");
        sb.append("name='").append(name).append('\'');
        sb.append(", familyName='").append(familyName).append('\'');
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
