package ro.ugal.learnromanian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.ugal.learnromanian.model.UserPhoto;

public interface UserPhotoRepository extends JpaRepository<UserPhoto, Integer> { }
