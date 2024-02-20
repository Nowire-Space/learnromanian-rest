package nowire.space.learnromanian.model;

import jakarta.persistence.*;
import lombok.*;

//TODO generalize this class to use with different exercise types(see GitHub project)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Integer exerciseId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "exercise_type")
    private ExerciseType exerciseType;


}