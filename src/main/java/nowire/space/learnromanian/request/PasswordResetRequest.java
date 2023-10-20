package nowire.space.learnromanian.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetRequest {

    @NotBlank
    @Size(max = 50, message = "{invalid.password.reset}")
    @Email
    private String email;
}
