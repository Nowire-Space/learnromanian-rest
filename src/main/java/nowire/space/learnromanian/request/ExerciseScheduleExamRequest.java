package nowire.space.learnromanian.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseScheduleExamRequest implements Serializable {
    private IdExercisesRequest idExercisesRequest;
    ScheduleExamRequest scheduleExamRequest;
}
