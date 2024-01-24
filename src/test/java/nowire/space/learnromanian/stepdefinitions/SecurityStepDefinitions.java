package nowire.space.learnromanian.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.mailjet.client.errors.MailjetException;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.LearnromanianRestApplication;
import nowire.space.learnromanian.model.Role;
import nowire.space.learnromanian.model.User;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ContextConfiguration(classes = LearnromanianRestApplication.class)
public class SecurityStepDefinitions {

    @Autowired
    MockMvc mockMvc;

    @Value("${webapp.url}")
    private String webAppUrl;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private EmailService emailServiceMock;

    private ObjectMapper objectMapper;

    private Integer savedUserId;

    private String bearerToken;

    private final ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);

    @Before("@Security")
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
    @Given("^validated user with following (.*), (.*), (.*), (.*) and (.*)$")
    public void save_validated_user(String userFamilyName, String userFirstName, String phoneNumber, String email,
                                          String password) {

        User user = User.builder()
                .userFamilyName(userFamilyName)

                .userFirstName(userFirstName)
                .userPhoneNumber(phoneNumber)
                .userEmail(email)
                .userPassword(password)
                .userActivated(true)
                .build();

        User savedUser = userRepository.save(user);
        savedUserId = savedUser.getUserId();

        log.info("Saved user email {}.", savedUser.getUserEmail());
    }
    @And("^active and enabled admin user with (.*), (.*), (.*), (.*) and (.*)$")
    public void save_admin_enabled(String adminFamilyName, String adminFirstName, String adminPhoneNumber,
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

    @When("^admin is logging with (.*) and (.*)$")
    public void admin_log_in(String adminEmail, String adminPassword) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/account/authenticate")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin",webAppUrl)
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

    @And("^admin enable the user account")
    public void admin_enable_user_account() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/enable")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin", webAppUrl)
                        .with(user("admin").roles("ADMIN"))
                        .content(objectMapper.writeValueAsString(
                                UserEnableRequest
                                        .builder()
                                        .userId(savedUserId)
                                        .roleId(Enum.Role_Id.MODERATOR)
                                        .build()))
                        .header("authorization", "Bearer ".concat(bearerToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(Message.ADMIN_VALIDATION_TRUE(
                        userRepository.findByUserId(savedUserId).get().getUserFirstName(),
                        userRepository.findByUserId(savedUserId).get().getUserFamilyName(),
                        userRepository.findByUserId(savedUserId).get().getUserEmail())));
        log.info("Admin approved registration request for user with id: {}.", savedUserId);
    }


    @Then("^users can access the user profile (.*)")
    public void usersCanAccessTheUserProfiles(String userProfile) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/account/{username}", userProfile)
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin",webAppUrl)
                        .with(user("user").roles("PROFESSOR")))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();
        response.getResponse().getContentAsString();
        userRepository.findByUserEmail(userProfile).get();
    }
}

