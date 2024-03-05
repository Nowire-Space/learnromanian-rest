package nowire.space.learnromanian.repository;

import nowire.space.learnromanian.model.FreeWritingExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeWritingExerciseRepository extends JpaRepository<FreeWritingExercise, Integer> {
}
