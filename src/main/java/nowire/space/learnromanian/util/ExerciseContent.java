package nowire.space.learnromanian.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseContent {
    private final String contentT_G = "This should be implemented to load the exam tests !";
    private String contentC_S;
    private String contentW_S;
}
