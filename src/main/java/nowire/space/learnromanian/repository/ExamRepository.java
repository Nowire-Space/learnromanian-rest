package nowire.space.learnromanian.repository;

import nowire.space.learnromanian.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {
    Exam findByName(String name);
    void deleteByName(String name);
}
