package nowire.space.learnromanian.repository;

import nowire.space.learnromanian.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    Exercise findByName(String name);
}
