package nowire.space.learnromanian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nowire.space.learnromanian.model.UserPhoto;

public interface UserPhotoRepository extends JpaRepository<UserPhoto, Integer> { }
