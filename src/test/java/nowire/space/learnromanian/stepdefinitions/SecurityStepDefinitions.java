package nowire.space.learnromanian.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ContextConfiguration(classes = LearnromanianRestApplication.class)
public class SecurityStepDefinitions {

    @Value("${webapp.url}")
    private String webAppUrl;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private String user;

    private String endpoint;

    private Integer responseStatusCode;

    @Given("^following roles$")
    public void save_roles(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        rows.forEach(row -> roleRepository.save(Role.builder()
                .roleId(Integer.valueOf(row.get("id")))
                .roleName(row.get("role"))
                .build()));

        log.info("Roles saved to DB: {}.", roleRepository.findAll());
    }

    @Given("^user with following$")
    public void save_user(DataTable table) {
        List<List<String>> rows = table.asLists(String.class);
        rows.forEach(row -> userRepository.save(User.builder()
                .userFamilyName(row.get(0))
                .userFirstName(row.get(1))
                .userPhoneNumber(row.get(2))
                .userEmail(row.get(3))
                .userPassword(encoder.encode(row.get(4)))
                .role(roleRepository.findByRoleId(1))
                .userEnabled(true)
                .userActivated(true)
                .build()));

        log.info("Saved users to H2 DB: {}.", userRepository.findAll());
    }

    @Given("^user (.*) is trying to access (.*) endpoint from the origin, API is configured for$")
    public void user_is_trying_to_access_endpoint(String user, String endpoint) {
        this.user = user;
        this.endpoint = endpoint;

        log.info("User {} is trying to access {} endpoint.", this.user, this.endpoint);
    }

    @Given("^user (.*) is trying to access (.*) endpoint from the wrong origin$")
    public void user_is_trying_to_access_endpoint_wrong_origin(String user, String endpoint) {
        this.user = user;
        this.endpoint = endpoint;
        webAppUrl = "http://someorigin.com";

        log.info("User {} is trying to access {} endpoint.", this.user, this.endpoint);
    }

    @When("^user (.*) sends POST authentication request with (.*) password$")
    public void user_sends_authentication_request(String email, String password) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/account/authenticate")
                        .content(objectMapper.writeValueAsString(
                                LoginRequest.builder()
                                        .username(email)
                                        .password(password)
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        responseStatusCode = response.getResponse().getStatus();

        log.info("Response to the OPTIONS request received.");
    }

    @When("^browser sends OPTIONS request with (.*) HTTP method for ACPM header$")
    public void browser_sends_options_request(String method) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.options(this.endpoint.concat(user))
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, method)
                        .header(HttpHeaders.ORIGIN, webAppUrl))
                .andDo(print())
                .andReturn();
        responseStatusCode = response.getResponse().getStatus();

        log.info("Response to the OPTIONS request received.");
    }

    @Then("^API responds with (.*) HTTP code$")
    public void api_responds(Integer expectedStatusCode) {
        assertThat(responseStatusCode).isEqualTo(expectedStatusCode);

        log.info("Response status is {}.", responseStatusCode);
    }
}

