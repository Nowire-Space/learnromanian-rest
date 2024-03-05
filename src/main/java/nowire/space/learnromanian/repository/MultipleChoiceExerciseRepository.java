package nowire.space.learnromanian.repository;

import nowire.space.learnromanian.model.MultipleChoiceExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipleChoiceExerciseRepository extends JpaRepository<MultipleChoiceExercise, Integer> {
}
