package nowire.space.learnromanian.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import nowire.space.learnromanian.util.Role;

@Data
public class UserEnableRequest {

    @NotBlank
    private Integer userId;

    private Role role;
}
