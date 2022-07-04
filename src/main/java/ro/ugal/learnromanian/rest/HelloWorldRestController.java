package ro.ugal.learnromanian.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/hello-world")
public class HelloWorldRestController {

    @GetMapping
    public ResponseEntity<String> helloWorldEndpoint() {
        return new ResponseEntity<>("Hello, World!", HttpStatus.OK);
    }
}
