package nowire.space.learnromanian.registration.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailjet.client.errors.MailjetException;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.LearnromanianRestApplication;
import nowire.space.learnromanian.configuration.util.Util;
import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.repository.RoleRepository;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.request.RegistrationRequest;
import nowire.space.learnromanian.request.UserEnableRequest;
import nowire.space.learnromanian.service.EmailService;
import nowire.space.learnromanian.util.Enum;
import nowire.space.learnromanian.util.Message;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ContextConfiguration(classes = LearnromanianRestApplication.class)
public class RegistrationStepDefinitions  extends Util {

    private static final String USER_ID = "USER_ID";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private EmailService emailServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    private final ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);

    @Before
    public void setUp() throws MailjetException {
        doNothing().when(emailServiceMock).sendValidationEmail(anyString(), anyString(), anyString(), valueCapture.capture());
    }

    @Given("user registration request with following: {word}, {word}, {word}, {word} and {word}")
    public void save_registration_request(String userFamilyName, String userFirstName, String phoneNumber, String email,
                                       String password) {
        testContext().setPayload(RegistrationRequest.builder()
                .userFamilyName(userFamilyName)
                .userFirstName(userFirstName)
                .userPhoneNumber(phoneNumber)
                .userEmail(email)
                .userPassword(password)
                .userPasswordCheck(password)
                .build());

        log.info("Saved registration request for user {}.", email);
    }

    @When("user {word} sends POST registration request")
    public void user_submitsPOST_registration_request(String email) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/account/create")
                        .content(objectMapper.writeValueAsString(testContext().getPayload()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        testContext().setResponseCode(response.getResponse().getStatus());
        testContext().set(USER_ID, userRepository.findByUserEmail(email).get().getUserId());

        log.info("POST request was submitted by new user: {}.", email);
        log.info("Captured validation token is: {}.", valueCapture.getValue());
    }

    @And("user {word} sends POST validation request")
    public void user_validates_provided_email_address(String email) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/account/validate/".concat(valueCapture.getValue())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(Message.USER_ACTIVATION_TRUE));

        log.info("Email was validated for user: {}.", email);
    }

    @And("user {word} sends POST reject request")
    public void admin_rejects_registration_request(String email) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/admin/reject/".concat(testContext().get(USER_ID).toString()))
                        .header("authorization", "Bearer ".concat(testContext().getBearerToken().toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        testContext().setResponseCode(response.getResponse().getStatus());

        log.info("Admin approved registration request for user with id: {}.",
                Optional.ofNullable(testContext().get(USER_ID)));
    }

    @And("user {word} sends POST enable request with {word} role")
    public void admin_approves_registration_request(String email, String role) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/admin/enable")
                        .content(objectMapper.writeValueAsString(
                                UserEnableRequest
                                        .builder()
                                        .userId(testContext().get(USER_ID))
                                        .roleId(Enum.Role_Id.STUDENT)
                                        .build()))
                        .header("authorization", "Bearer ".concat(testContext().getBearerToken().toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        testContext().setResponseCode(response.getResponse().getStatus());

        log.info("Admin approved registration request for user with id: {}.",
                Optional.ofNullable(testContext().get(USER_ID)));
    }

    @Then("user's data is saved to db and user's account is enabled")
    public void user_s_data_is_saved_to_db_and_account_is_enabled() {
        User savedUser = userRepository.findByUserId(testContext().get(USER_ID)).get();
        assertThat(savedUser.getUserId()).isEqualTo(testContext().get(USER_ID));
        assertThat(savedUser.isUserEnabled()).isTrue();
        assertThat(savedUser.isUserActivated()).isTrue();

        log.info("Assertions passed.");
    }

    @Then("user's data is erased from DB")
    public void user_s_data_is_erased_from_db() {
        Optional<User> savedUser = userRepository.findByUserId(testContext().get(USER_ID));
        assertThat(savedUser.isPresent()).isFalse();

        log.info("Assertions passed.");
    }

    @After
    public void cleanUp() {
        testContext().reset();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        log.info("Deleted all users and roles from DB.");
    }
}
