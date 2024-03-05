package nowire.space.learnromanian.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChoiceRequest extends ExerciseRequest {

    private String correctAnswer;

}
