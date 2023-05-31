package nowire.space.learnromanian.stepdefinitions;

import com.google.inject.Inject;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import nowire.space.learnromanian.controller.AccountController;
import nowire.space.learnromanian.model.User;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
public class RegistrationStepDefinitions {

    @Inject
    @MockBean
    private AccountController accountController;

    @Given("^user with (.*) and (.*)$")
    public void user_with_doe_and_john(String firstName, String lastName) {
        User newUser = new User();
        newUser.setUserFamilyName(lastName);
        newUser.setUserFirstName(firstName);
        when(accountController.createAccount(any())).thenReturn(new ResponseEntity<>("OK", HttpStatus.OK));
        log.info("First Name: {}. Last Name: {}, id: {}", firstName, lastName, accountController.createAccount(newUser));
    }
}
