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
import nowire.space.learnromanian.model.Team;
import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.repository.RoleRepository;
import nowire.space.learnromanian.repository.TeamRepository;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.request.LoginRequest;
import nowire.space.learnromanian.request.RegistrationRequest;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
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
    private PasswordEncoder passwordEncoder;


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TeamRepository teamRepository;

    @MockBean
    private EmailService emailServiceMock;

    private ObjectMapper objectMapper;

    private TeamRequest teamRequest;

    private final ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    private String bearerToken;

    @Before("@Team")
    public void setUp() throws MailjetException {
        objectMapper = new ObjectMapper();

        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, Enum.Role.ADMIN));
        roles.add(new Role(2, Enum.Role.MODERATOR));
        roles.add(new Role(3, Enum.Role.PROFESSOR));
        roles.add(new Role(4, Enum.Role.STUDENT));
        List<Role> savedRoles = roleRepository.saveAll(roles);
        log.info("Roles saved to DB: {}", savedRoles);

        doNothing().when(emailServiceMock).sendValidationEmail(anyString(), anyString(), anyString(), valueCapture.capture());
    }

    //TODO
    @Given("^active team admin and professor users with (.*), (.*), (.*), (.*), (.*), (.*), (.*), (.*), (.*) and (.*)$")
    public void save_admin_user(String familyName, String firstName, String phoneNumber, String email,
                                          String password, String professorFamilyName, String professorFirstName, String professorPhoneNumber,
                                String professorEmail, String professorPassword) {

        passwordEncoder.encode("john.doe");

        User adminUser = User.builder()
                .userFamilyName(familyName)
                .userFirstName(firstName)
                .userPhoneNumber(phoneNumber)
                .userEmail(email)
                .userPassword(encoder.encode(password))
                .role(roleRepository.findByRoleId(1))
                .userEnabled(true)
                .userActivated(true)
                .build();

                User professorUser = User.builder()
                .userFamilyName(professorFamilyName)
                .userFirstName(professorFirstName)
                .userPhoneNumber(professorPhoneNumber)
                .userEmail(professorEmail)
                .userPassword(encoder.encode(professorPassword))
                .role(roleRepository.findByRoleId(3))
                .userEnabled(true)
                .userActivated(true)
                .build();

        User savedAdminUser = userRepository.save(adminUser);
        User savedProfessorUser = userRepository.save(professorUser);
        log.info("Saved users {}, {} with role {}, {} to the H2 DB.", savedAdminUser.getUserEmail(), savedProfessorUser.getUserEmail(),
                savedAdminUser.getRole().getRoleName(), savedProfessorUser.getRole().getRoleName());
    }

    @When("^team admin proceeds with log in with (.*) and (.*)$")
    public void admin_proceeds_with_log_in(String adminEmail, String adminPassword) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/account/authenticate")
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

    @When("^admin user submits POST team request for team creation with (.*) and (.*)$")
    public void user_submitsPOST_new_team_request(String teamName, String teamDescription) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/team/create")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin",webAppUrl)
                        .with(user("john.doe@mail.com").roles("PROFESSOR"))
                        .content(objectMapper.writeValueAsString(TeamRequest.builder().name(teamName).description(teamDescription).build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$")
                        .value(Message.TEAM_CREATED(teamName,teamDescription)));

        log.info("Admin created new team: {}.", teamName);
    }


    @Given("^team registration request with (.*) and (.*)$")
    public void teamRegistrationRequestWithAnd(String name, String description) {
        Set <User> users = new HashSet<>();
        teamRequest = TeamRequest.builder().name(name).description(description).students(users).build();
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
       userRepository.save(user);

       return new ResponseEntity<>("The user with the role" + user.getRole() + "username" + user.getUserFamilyName()+ "is saved in DB",HttpStatus.OK);
    }

    @And("^active and enabled student with (.*), (.*), (.*), (.*) and (.*)$")
    public ResponseEntity<String> saveStudent(String studentFamilyName, String studentFirstName, String studentPhoneNumber, String studentEmail, String studentPassword) {
        User student = User.builder().userFamilyName(studentFamilyName)
                .userFirstName(studentFirstName)
                .userPhoneNumber(studentPhoneNumber)
                .userEmail(studentEmail)
                .userPassword(encoder.encode(studentPassword))
                .userActivated(true)
                .userEnabled(true)
                .role(roleRepository.findByRoleId(4))
                .build();
        userRepository.save(student);

        return new ResponseEntity<>("The student with the role" + student.getRole() + "username" + student.getUserFamilyName()+ "is saved in DB",HttpStatus.OK);


    }
    @When("^user submit POST team request for team creation !")
    @WithMockUser(username = "john.doe@mail.com")
    public void teamCreation () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/team/create")
//                        .header("Access-Control-Request-Method", "POST")
//                        .header("Origin",webAppUrl)
                        .with(user("john.doe@mail.com").roles("PROFESSOR"))
                        .content(objectMapper.writeValueAsString(teamRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$")
                        .value(Message.TEAM_CREATED(teamRequest.getName(),teamRequest.getDescription())));
                log.info("POST request was submitted for team name: {}  and team description: {}.", teamRequest.getName(), teamRequest.getDescription());
    }

    @Then("^student (.*) is added to the team (.*).$")
    public void userIsAddedToTheTeam(String studentEmail, String name) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/team/addStudent/{username}/{teamName}", studentEmail, name).header("Access-Control-Request-Method", "POST")
                        .header("Origin",webAppUrl)
                        .with(user("john.doe@mail.com").roles("PROFESSOR"))
                        .content(objectMapper.writeValueAsString(teamRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(Message.USER_ADDED_TO_THE_TEAM(studentEmail)));
        log.info("Student with the Family Name : {} and First Name: {} was added to the team  {} ", userRepository.findByUserEmail(studentEmail).get().getUserFamilyName()
                ,userRepository.findByUserEmail(studentEmail).get().getUserFirstName(),teamRequest.getDescription());
    }

    @And("^user with the email address (.*) added to the team (.*).")
    public void userWithTheEmailAddressAddedToTheTeam(String username, String name) throws Exception {
        User user = userRepository.findByUserEmail(username).get();
        Team team = Team.builder().name(teamRequest.getName()).description(teamRequest.getDescription()).students(teamRequest.getStudents()).build();
        team.setStudents(Set.of(user));
        teamRepository.save(team);
    }

    @And("^user make a delete request for the username (.*) from the team (.*)")
    public void userMakeADeleteRequestForTheUsernameFromTheTeam(String studentEmail, String name) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/team/remove/{username}/{teamName}", studentEmail, name).header("Access-Control-Request-Method", "DELETE")
                        .header("Origin", webAppUrl)
                        .with(user("john.doe@mail.com").roles("PROFESSOR"))
                        .content(objectMapper.writeValueAsString(teamRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(Message.USER_REMOVED_FROM_THE_TEAM(studentEmail)));
    }


    @Then("^user with the username (.*) is deleted from the team.")
    public void userWithTheUsernameIsDeletedFromTheTeam(String username) {
        Optional<User> savedUser = userRepository.findByUserEmail(username);
        User saved  = savedUser.get();
        assertThat(savedUser.isPresent()).isFalse();
        log.info("Assertions passed.");
    }

    @Then("^new team with (.*), (.*) is created and team head is (.*)$")
    public void newTeamWithIsCreatedAndTeamHeadIs(String teamName, String teamDescription, String professorEmail) {
        Team teamCreated = teamRepository.findByName(teamName);
        assertThat(teamCreated.getDescription()==teamDescription && teamCreated.getTeamHead().getUserEmail()==professorEmail).isFalse();
        log.info("Assertion passed !" + teamCreated.getDescription() + teamCreated.getName() + teamCreated.getTeamHead().getUserEmail());

    }
}

