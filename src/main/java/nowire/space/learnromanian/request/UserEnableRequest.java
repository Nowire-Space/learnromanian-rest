package nowire.space.learnromanian.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEnableRequest {

    @NotNull
    private Integer userId;

    @NotNull
    private Integer roleId;
}
