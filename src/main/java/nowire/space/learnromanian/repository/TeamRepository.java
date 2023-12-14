package nowire.space.learnromanian.repository;

import nowire.space.learnromanian.model.Team;
import nowire.space.learnromanian.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
   Team findByName(String name);




}
