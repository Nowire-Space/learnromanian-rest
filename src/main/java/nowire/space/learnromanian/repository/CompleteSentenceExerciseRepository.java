package nowire.space.learnromanian.repository;

import nowire.space.learnromanian.model.CompleteSentenceExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompleteSentenceExerciseRepository extends JpaRepository<CompleteSentenceExercise, Integer> {
}
