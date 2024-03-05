package nowire.space.learnromanian.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("complete_sentence")
public class CompleteSentenceExercise extends Exercise {

    @Column(name = "sentence")
    private String sentence;

    @Column(name="positionAnswers")
    private String positionAnswers;


}
