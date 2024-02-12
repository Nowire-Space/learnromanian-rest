package nowire.space.learnromanian.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nowire.space.learnromanian.model.ExerciseType;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRequest {
    private String name;
    private String description;
    private ExerciseType exerciseType;

}
