package nowire.space.learnromanian.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import java.util.Objects;

@Data
public class RegistrationRequest {

    @NotBlank
    @Size(min = 2, max = 20)
    private String userFirstName;

    @NotBlank
    @Size(min = 2, max = 20)
    private String userFamilyName;

    @NotBlank
    @Size(max = 15)
    @NumberFormat
    private String userPhoneNumber;

    @NotBlank
    @Size(max = 50)
    @Email
    private String userEmail;

    @NotBlank
    @Size(min = 6, max = 20)
    private String userPassword;

    @NotBlank
    @Size(min = 6, max = 20)
    private String userPasswordCheck;

    @AssertTrue
    private boolean isCheckPasswordEqualToPassword() {
        return Objects.equals(userPasswordCheck, userPassword);
    }
}
