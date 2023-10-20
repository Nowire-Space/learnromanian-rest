package nowire.space.learnromanian.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nowire.space.learnromanian.validator.CreateAccountConstraint;
import org.springframework.format.annotation.NumberFormat;
import java.util.Objects;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@CreateAccountConstraint
public class RegistrationRequest {

    @NotBlank
    @Size(min = 2, max = 20, message = "{invalid.length}")
    private String userFirstName;

    @NotBlank
    @Size(min = 2, max = 20, message = "{invalid.length}")
    private String userFamilyName;

    @NotBlank
    @Size(max = 15, message = "{invalid.phone.no}")
    @NumberFormat
    private String userPhoneNumber;

    @NotBlank
    @Size(max = 50, message = "{invalid.user.email}")
    @Email
    private String userEmail;

    @NotBlank
    @Size(min = 6, max = 20, message = "{invalid.user.password}")
    private String userPassword;

    @NotBlank
    @Size(min = 6, max = 20, message = "{invalid.user.password.check}")
    private String userPasswordCheck;

    @AssertTrue
    private boolean isCheckPasswordEqualToPassword() {
        return Objects.equals(userPasswordCheck, userPassword);
    }
}
