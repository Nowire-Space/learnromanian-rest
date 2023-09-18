package nowire.space.learnromanian.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.mailjet.client.errors.MailjetException;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.LearnromanianRestApplication;
import nowire.space.learnromanian.model.Role;
import nowire.space.learnromanian.repository.RoleRepository;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.request.LoginRequest;
import nowire.space.learnromanian.request.RegistrationRequest;
import nowire.space.learnromanian.request.UserEnableRequest;
import nowire.space.learnromanian.service.EmailService;
import nowire.space.learnromanian.util.Enum;
import nowire.space.learnromanian.util.Message;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import nowire.space.learnromanian.model.User;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ContextConfiguration(classes = LearnromanianRestApplication.class)
public class RegistrationStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private EmailService emailServiceMock;

    private ObjectMapper objectMapper;

    private RegistrationRequest registrationRequest;

    private Integer savedUserId;

    private String bearerToken;

    private Integer errorCode;

    private String temporaryResponse;

    private final ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);

    @Before("@Registration")
    public void setUp() throws MailjetException {
        objectMapper = new ObjectMapper();

        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, Enum.Role.ADMIN));
        roles.add(new Role(2, Enum.Role.MODERATOR));
        roles.add(new Role(3, Enum.Role.PROFESSOR));
        roles.add(new Role(4, Enum.Role.STUDENT));
        roleRepository.saveAll(roles);
        log.info("Roles saved to DB.");

        doNothing().when(emailServiceMock).sendValidationEmail(anyString(), anyString(), anyString(), valueCapture.capture());
    }

    @Given("^user with following (.*), (.*), (.*), (.*), (.*) and (.*)$")
    public void save_registration_request(String userFamilyName, String userFirstName, String phoneNumber, String email,
                                       String password, String passwordCheck) {
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

    @And("^active admin user with (.*), (.*), (.*), (.*) and (.*)$")
    public void save_admin_user(String adminFamilyName, String adminFirstName, String adminPhoneNumber,
                                String adminEmail, String adminPassword) {
        User adminUser = User.builder()
                .userFamilyName(adminFamilyName)
                .userFirstName(adminFirstName)
                .userPhoneNumber(adminPhoneNumber)
                .userEmail(adminEmail)
                .userPassword(encoder.encode(adminPassword))
                .role(roleRepository.findByRoleId(1))
                .userEnabled(true)
                .userActivated(true)
                .build();

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
        log.info("POST request was submitted by new user: {}.", registrationRequest.getUserFirstName().concat(" ")
                .concat(registrationRequest.getUserFamilyName()));
        log.info("captured token is: {}.", valueCapture.getValue());
    }

    @And("user validates provided email address")
    public void user_validates_provided_email_address() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/account/validate/".concat(valueCapture.getValue()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(Message.USER_ACTIVATION_TRUE));
        log.info("Email was validated for user: {}.", savedUserId);
    }

    @When("user submits POST registration request without email")
    public void user_submitsPOST_registration_request_without_email() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/account/create")
                        .content(objectMapper.writeValueAsString(registrationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        errorCode = response.getResponse().getStatus();
        log.info("POST request was submitted without email.");
    }

    @And("^admin proceeds with log in with (.*) and (.*)$")
    public void admin_proceeds_with_log_in(String adminEmail, String adminPassword) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/account/authenticate")
                        .content(objectMapper.writeValueAsString(
                                LoginRequest
                                        .builder()
                                        .username(adminEmail)
                                        .password(adminPassword)
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();

        bearerToken = JsonPath.read(response.getResponse().getContentAsString(), "$.token");
        log.info("Admin auth token is: {}.", bearerToken);
    }

    @And("admin approves registration request for not enabled account")
    public void admin_approves_registration_request_for_not_enabled_account() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/admin/enable")
                        .content(objectMapper.writeValueAsString(
                                UserEnableRequest
                                        .builder()
                                        .userId(savedUserId)
                                        .roleId(Enum.Role_Id.STUDENT)
                                        .build()))
                        .header("authorization", "Bearer ".concat(bearerToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(Message.USER_NOT_ACTIVATED(savedUserId)))
                .andReturn();
        temporaryResponse = JsonPath.read(response.getResponse().getContentAsString(), "$");
        log.info("Admin could not approve registration request for user id: {}.", savedUserId);
    }

    @And("admin rejects registration request")
    public void admin_rejects_registration_request() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/reject/".concat(savedUserId.toString()))
                        .header("authorization", "Bearer ".concat(bearerToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$")
                        .value(Message.ADMIN_REJECT_TRUE(savedUserId.toString())));
        log.info("Admin approved registration request for user with id: {}.", savedUserId);
    }

    @And("admin approves registration request")
    public void admin_approves_registration_request() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/enable")
                        .content(objectMapper.writeValueAsString(
                                UserEnableRequest
                                        .builder()
                                        .userId(savedUserId)
                                        .roleId(Enum.Role_Id.STUDENT)
                                        .build()))
                        .header("authorization", "Bearer ".concat(bearerToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(Message.ADMIN_VALIDATION_TRUE(
                        registrationRequest.getUserFirstName(), registrationRequest.getUserFamilyName(),
                        registrationRequest.getUserEmail())));
        log.info("Admin approved registration request for user with id: {}.", savedUserId);
    }

    @Then("user's data is saved to db and user's account is enabled")
    public void user_s_data_is_saved_to_db_and_account_is_enabled() {
        User savedUser = userRepository.findByUserId(savedUserId).get();
        assertThat(savedUser.getUserEmail()).isEqualTo(registrationRequest.getUserEmail());
        assertThat(savedUser.getUserFirstName()).isEqualTo(registrationRequest.getUserFirstName());
        assertThat(savedUser.getUserFamilyName()).isEqualTo(registrationRequest.getUserFamilyName());
        assertThat(savedUser.isUserEnabled()).isTrue();
        log.info("Assertions passed.");
    }

    @Then("admin receives error response")
    public void admin_receives_error_response() {
        assertThat(temporaryResponse).isEqualTo(Message.USER_NOT_ACTIVATED(savedUserId));
        log.info("Assertions passed.");
    }

    @Then("user's data is erased from DB")
    public void user_s_data_is_erased_from_db() {
        Optional<User> savedUser = userRepository.findByUserId(savedUserId);
        assertThat(savedUser.isPresent()).isFalse();
        log.info("Assertions passed.");
    }

    @Then("^(.*) is returned$")
    public void error_message_is_returned(Integer errorCode) {
        assertThat(this.errorCode).isEqualTo(errorCode);
        log.info("Assertions passed.");
    }

    @After
    public void cleanUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        log.info("Deleted all users and roles from DB.");
    }
}
