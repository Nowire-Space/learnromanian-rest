package nowire.space.learnromanian.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
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
import nowire.space.learnromanian.configuration.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ContextConfiguration(classes = LearnromanianRestApplication.class)
public class GenericStepDefinitions extends Util {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Given("following roles:")
    public void save_roles(List<Role> roles) {
        roleRepository.saveAll(roles);

        log.info("Roles saved to DB: {}.", roleRepository.findAll());
    }

    @Given("{word} users with following:")
    public void save_user(String role, List<User> users) {
        Role assignedRole = roleRepository.findByRoleName(role);
        users.forEach(user -> user.setRole(assignedRole));
        userRepository.saveAll(users);

        log.info("Saved users to H2 DB: {}.", userRepository.findAll());
    }

    @When("user {word} sends POST authentication request with {word} password")
    public void user_sends_authentication_request(String email, String password) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/account/authenticate")
                        .content(objectMapper.writeValueAsString(
                                LoginRequest.builder()
                                        .username(email)
                                        .password(password)
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        testContext().setResponseCode(response.getResponse().getStatus());
        if (testContext().getResponseCode().equals(HttpStatus.OK.value()))
            testContext().setBearerToken(JsonPath.read(response.getResponse().getContentAsString(), "$.token"));
    }

    @Then("API responds with {int} HTTP code")
    public void api_responds(Integer expectedStatusCode) {
        assertThat(testContext().getResponseCode()).isEqualTo(expectedStatusCode);

        log.info("Response status is {}.", testContext().getResponseCode());
    }
}
