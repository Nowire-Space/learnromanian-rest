package nowire.space.learnromanian.repository;

import nowire.space.learnromanian.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> { }
