package nowire.space.learnromanian.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.LearnromanianRestApplication;
import nowire.space.learnromanian.model.Role;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.request.RegistrationRequest;
import nowire.space.learnromanian.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import nowire.space.learnromanian.model.User;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ContextConfiguration(classes = LearnromanianRestApplication.class)
public class RegistrationStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    ObjectMapper objectMapper;

    RegistrationRequest registrationRequest;

    Integer savedUserId;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Given("^user with following (.*), (.*), (.*), (.*), (.*) and (.*)$")
    public void save_registration_request(String userFamilyName, String userFirstName, String phoneNumber, String email,
                                       String password, String passwordCheck) throws IOException {
        registrationRequest = RegistrationRequest.builder()
                .userFamilyName(userFamilyName)
                .userFirstName(userFirstName)
                .userPhoneNumber(phoneNumber)
                .userEmail(email)
                .userPassword(password)
                .userPasswordCheck(passwordCheck)
                .build();

        log.info("Saved registration request for user {}.", registrationRequest.getUserEmail());
    }

    @And("^admin user with (.*), (.*), (.*), (.*) and (.*)$")
    public void save_admin_user(String adminFamilyName, String adminFirstName, String adminPhoneNumber,
                                String adminEmail, String adminPassword) {
        User adminUser = User.builder()
                .userFamilyName(adminFamilyName)
                .userFirstName(adminFirstName)
                .userPhoneNumber(adminPhoneNumber)
                .userEmail(adminEmail)
                .userPassword(adminPassword)
                .userEnabled(false)
                .build();

        adminUser.setRole(Role.builder()
                .roleName(nowire.space.learnromanian.util.Role.ADMIN.toString())
                .build());

        User savedAdminUser = userRepository.save(adminUser);
        log.info("Saved user {} with role {} to H2 DB.", savedAdminUser.getUserEmail(), savedAdminUser.getRole().getRoleName());
    }

    @When("user submits POST registration request")
    public void user_submitsPOST_registration_request() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/account/create")
                        .content(objectMapper.writeValueAsString(registrationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(Message.USER_REGISTRATION_TRUE(
                        registrationRequest.getUserFirstName(), registrationRequest.getUserFamilyName(),
                        registrationRequest.getUserEmail())));
        savedUserId = userRepository.findByUserEmail(registrationRequest.getUserEmail()).get().getUserId();
        log.info("POST request was submitted by new user: {}", registrationRequest.getUserFirstName().concat(" ")
                .concat(registrationRequest.getUserFamilyName()));
    }

    @And("admin approves registration request")
    public void admin_approves_registration_request() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/enable/".concat(String.valueOf(savedUserId)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(Message.ADMIN_VALIDATION_SUCCESS(
                        registrationRequest.getUserFirstName(), registrationRequest.getUserFamilyName(),
                        registrationRequest.getUserEmail())));
    }

    @Then("user's data is saved to db and user's account is enabled")
    public void user_s_data_is_saved_to_db_and_account_is_enabled() {
        User savedUser = userRepository.findByUserId(savedUserId);
        assertThat(savedUser.getUserEmail()).isEqualTo(registrationRequest.getUserEmail());
        assertThat(savedUser.getUserFirstName()).isEqualTo(registrationRequest.getUserFirstName());
        assertThat(savedUser.getUserFamilyName()).isEqualTo(registrationRequest.getUserFamilyName());
        assertThat(savedUser.isUserEnabled()).isTrue();
        log.info("Assertions passed.");
    }
}
