package nowire.space.learnromanian.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exam")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    protected Integer examId;

    @OneToOne
    @JoinColumn(name = "team_id")
    protected Team team;

    @Column(name = "name", unique = true)
    protected String name;

    @Column(name = "scheduled_exam")
    protected LocalDateTime scheduleExam;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "exercise_id")
    protected Exercise exercise;

}
