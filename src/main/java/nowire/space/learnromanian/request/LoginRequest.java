package nowire.space.learnromanian.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nowire.space.learnromanian.validator.AuthenticateConstraint;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuthenticateConstraint
public class LoginRequest {

    @NotBlank (message = "{invalid_username}")
    private String username;


    @NotBlank(message = "{invalid_password}")
    private String password;

}
