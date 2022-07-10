package ro.ugal.learnromanian.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.ugal.learnromanian.service.SpecializationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/specialization")
public class SpecializationRestController {

    private final SpecializationService specializationService;

    public SpecializationRestController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAllAvailableSpecializationsForRegistration() {
        return new ResponseEntity<>(specializationService.getAllAvailableSpecializationsForRegistration(), HttpStatus.OK);
    }
}
