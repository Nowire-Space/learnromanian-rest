package nowire.space.learnromanian.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nowire.space.learnromanian.validator.ValidateAccountConstraint;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidateAccountConstraint
public class ValidateRequest {
    private String token;
}

