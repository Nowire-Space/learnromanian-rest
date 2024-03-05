package nowire.space.learnromanian.repository;

import nowire.space.learnromanian.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {

}
