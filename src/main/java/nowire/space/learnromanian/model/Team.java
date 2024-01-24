package nowire.space.learnromanian.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    protected Integer team_id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "team", orphanRemoval = true)
    private Set<User> students;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private User teamHead;

    public void addUser(User user){
        students.add(user);
        user.setTeam(this);
    }

}
