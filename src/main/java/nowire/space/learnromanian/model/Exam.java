package nowire.space.learnromanian.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "name")
    protected String name;

    @Column(name = "scheduled_exam")
    protected LocalDateTime scheduleExam;

}
