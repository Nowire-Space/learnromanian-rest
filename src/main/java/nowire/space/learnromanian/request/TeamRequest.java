package nowire.space.learnromanian.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nowire.space.learnromanian.model.User;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequest {

    private String name;
    private String description;
    private Set<User> students;

}
