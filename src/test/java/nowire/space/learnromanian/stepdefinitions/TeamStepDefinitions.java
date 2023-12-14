package nowire.space.learnromanian.stepdefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import nowire.space.learnromanian.request.LoginRequest;
import nowire.space.learnromanian.request.TeamRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MultiValueMap;

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
public class TeamStepDefinitions {
    @Autowired
    MockMvc mockMvc;

    @Value("${webapp.url}")
    private String webAppUrl;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private EmailService emailServiceMock;

    private ObjectMapper objectMapper;

    private TeamRequest teamRequest;

    private final ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);

    private String bearerToken;

    @Autowired
    private PasswordEncoder encoder;


    @Before("@Team")
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

    @Given("^team registration request with (.*) and (.*)$")
    public void teamRegistrationRequestWithAnd(String name, String description) {
        teamRequest = TeamRequest.builder().name(name).description(description).build();
        log.info("team with {} was created", name);
    }

    @And("^active and enabled user with (.*), (.*), (.*), (.*) and (.*)$")
    public ResponseEntity<String> save_user (String userFamilyName, String userFirstName, String phoneNumber, String email,
                                     String password) {
       User user = User.builder().userFamilyName(userFamilyName)
               .userFirstName(userFirstName)
               .userPhoneNumber(phoneNumber)
               .userEmail(email)
               .userPassword(encoder.encode(password))
               .userActivated(true)
               .userEnabled(true)
               .role(roleRepository.findByRoleId(3))
               .build();

       return new ResponseEntity<>("The user with the role" + user.getRole() + "username" + user.getUserFamilyName()+ "is saved in DB",HttpStatus.OK);
    }

    @And("^active and enabled student with (.*), (.*), (.*), (.*) and (.*)$")
    public ResponseEntity<String> saveStudent(String studentFamilyName, String studentFirstName, String studentPhoneNumber, String studentEmail, String studentPassword) {
        User user = User.builder().userFamilyName(studentFamilyName)
                .userFirstName(studentFirstName)
                .userPhoneNumber(studentPhoneNumber)
                .userEmail(studentEmail)
                .userPassword(encoder.encode(studentPassword))
                .userActivated(true)
                .userEnabled(true)
                .role(roleRepository.findByRoleId(4))
                .build();

        return new ResponseEntity<>("The student with the role" + user.getRole() + "username" + user.getUserFamilyName()+ "is saved in DB",HttpStatus.OK);


    }
    @When("^user submit POST team request for team creation !")
    @WithMockUser(username = "john.doe@mail.com")
    public void teamCreation () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/team/create").header("Access-Control-Request-Method", "POST")
                        .header("Origin",webAppUrl)
                        .with(user("john.doe@mail.com").roles("PROFESSOR"))
                        .content(objectMapper.writeValueAsString(teamRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(Message.TEAM_CREATED(teamRequest.getName(),teamRequest.getDescription())));
                log.info("POST request was submitted for team name: {}  and team description: {}.", teamRequest.getName(), teamRequest.getDescription());
    }


    @Then("user is added to the team.")
    public void userIsAddedToTheTeam() {
    }

    @When("^user is logging with (.*) and (.*)$")
    public void userIsLoggingWithAnd(String email, String password) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/account/authenticate").header("Access-Control-Request-Method", "POST")
                        .header("Origin",webAppUrl)
                        .content(objectMapper.writeValueAsString(
                                LoginRequest
                                        .builder()
                                        .username(email)
                                        .password(password)
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();

        bearerToken = JsonPath.read(response.getResponse().getContentAsString(), "$.token");
        log.info("Admin auth token is: {}.", bearerToken);
    }
    }

