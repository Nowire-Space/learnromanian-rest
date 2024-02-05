package nowire.space.learnromanian.security.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.LearnromanianRestApplication;
import nowire.space.learnromanian.configuration.util.Util;
import nowire.space.learnromanian.repository.RoleRepository;
import nowire.space.learnromanian.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ContextConfiguration(classes = LearnromanianRestApplication.class)
public class SecurityStepDefinitions extends Util {

    @Value("${webapp.url}")
    private String webAppUrl;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private String user;

    private String endpoint;

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

    @When("user {word} sends GET all profiles request with saved token")
    public void user_sends_all_profiles_request(String userEmail) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/account")
                        .param("page", "1")
                        .param("rowsPerPage", "10")
                        .header("authorization", "Bearer ".concat(testContext().getBearerToken().toString())))
                .andDo(print())
                .andReturn();
        testContext().setResponseCode(response.getResponse().getStatus());

        log.info("Response to the OPTIONS request received.");
    }

    @When("user {word} sends GET profile request with saved token")
    public void user_sends_profile_request(String userEmail) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/account/".concat(userEmail))
                        .header("authorization", "Bearer ".concat(testContext().getBearerToken().toString())))
                .andDo(print())
                .andReturn();
        testContext().setResponseCode(response.getResponse().getStatus());

        log.info("Response to the OPTIONS request received.");
    }

    @When("^browser sends OPTIONS request with (.*) HTTP method for ACPM header$")
    public void browser_sends_options_request(String method) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.options(this.endpoint.concat(user))
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, method)
                        .header(HttpHeaders.ORIGIN, webAppUrl))
                .andDo(print())
                .andReturn();
        testContext().setResponseCode(response.getResponse().getStatus());

        log.info("Response to the OPTIONS request received.");
    }

    @After
    public void clearDb() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}
