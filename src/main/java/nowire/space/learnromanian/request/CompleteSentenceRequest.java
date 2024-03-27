package nowire.space.learnromanian.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteSentenceRequest extends ExerciseRequest{

    private String sentence;
    private String positionAnswers;
}
