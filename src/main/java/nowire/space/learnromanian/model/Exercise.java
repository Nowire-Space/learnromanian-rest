package nowire.space.learnromanian.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

//TODO generalize this class to use with different exercise types(see GitHub project)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exercise_hierarchy")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name ="exercise_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("EXERCISE")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Integer exerciseId;

    @Size(max=250)
    @Column(name = "question")
    private String question;

    @Column(name = "studentAnswers")
    private String answer;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id")
    private Exam exam;

}
