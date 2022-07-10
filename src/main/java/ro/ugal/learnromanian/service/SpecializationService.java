package ro.ugal.learnromanian.service;

import org.springframework.stereotype.Service;
import ro.ugal.learnromanian.model.Specialization;
import ro.ugal.learnromanian.repository.SpecializationRepository;

import java.util.List;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public List<Specialization> getAllAvailableSpecializationsForRegistration() {
        return specializationRepository.findAll();
    }
}
