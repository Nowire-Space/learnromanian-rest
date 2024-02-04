package nowire.space.learnromanian.profile.stepdefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.After;
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
import nowire.space.learnromanian.util.Enum;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ContextConfiguration(classes = LearnromanianRestApplication.class)
public class ProfileStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    private User user;

    private ObjectMapper objectMapper;

    private String bearerToken;

    private String txt;

    @Before("@Profile")
    public void setUp() {
        objectMapper = new ObjectMapper();
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, Enum.Role.ADMIN));
        roles.add(new Role(2, Enum.Role.MODERATOR));
        roles.add(new Role(3, Enum.Role.PROFESSOR));
        roles.add(new Role(4, Enum.Role.STUDENT));
        roleRepository.saveAll(roles);
    }

    @Given("^active and enabled user with following (.*), (.*), (.*), (.*), (.*)$")
    public void user_is_active_and_enabled(String userFamilyName, String userFirstName, String phoneNumber, String email,
                                           String password) {
        User user = User.builder()
                .userFamilyName(userFamilyName)
                .userFirstName(userFirstName)
                .userPhoneNumber(phoneNumber)
                .userEmail(email)
                .userPassword(encoder.encode(password))
                .role(roleRepository.findByRoleId(4))
                .userEnabled(true)
                .userActivated(true)
                .build();
        User savedUser = userRepository.save(user);
        log.info("Saved user {} with role {} to H2 DB.", savedUser.getUserEmail(), savedUser.getRole().getRoleName());
        log.info("user {}", user);
    }

    @When("^user proceeds with log in with (.*) and (.*)$")
    public void user_log_in(String email, String password) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/account/authenticate")
                        .content(objectMapper.writeValueAsString(
                                LoginRequest.builder()
                                        .username(email)
                                        .password(password)
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();

        bearerToken = JsonPath.read(response.getResponse().getContentAsString(), "$.token");
        log.info("User auth token is: {}.", bearerToken);
    }

    @And("^user submits GET profile request for the (.*)")
    @WithMockUser(username = "username", roles = {"STUDENT"})
    public void get_profile_request(String userProfile) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/account/{username}", userProfile)
                        .with(user("user").roles("STUDENT")))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();
        txt = response.getResponse().getContentAsString();
        user = userRepository.findByUserEmail(userProfile).get();
    }

    @Then("user's data is pulled")
    public void data_pulled() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(user);
        if (txt.equals(json)) {
            log.info("Data is correctly returned");
        } else {
            log.error("Data is wrong returned");
        }
    }

    @After
    public void clearDb() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}