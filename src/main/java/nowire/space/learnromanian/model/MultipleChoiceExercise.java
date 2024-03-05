package nowire.space.learnromanian.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("multiple_choice")
public class MultipleChoiceExercise extends Exercise{

    @Column(name = "correctAnswer")
    private String correctAnswer;
}
