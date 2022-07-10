package ro.ugal.learnromanian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.ugal.learnromanian.model.Specialization;

public interface SpecializationRepository extends JpaRepository<Specialization, Integer> {
    Specialization getBySpecializationName(String specializationName);
}
