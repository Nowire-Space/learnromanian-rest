package nowire.space.learnromanian.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEnableRequest {

    @NotNull
    private Integer userId;

    @NotNull
    private Integer roleId;
}
