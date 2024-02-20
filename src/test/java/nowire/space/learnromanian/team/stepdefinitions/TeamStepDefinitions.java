package nowire.space.learnromanian.team.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.LearnromanianRestApplication;
import nowire.space.learnromanian.configuration.util.Util;
import nowire.space.learnromanian.model.Team;
import nowire.space.learnromanian.model.User;
import nowire.space.learnromanian.repository.RoleRepository;
import nowire.space.learnromanian.repository.TeamRepository;
import nowire.space.learnromanian.repository.UserRepository;
import nowire.space.learnromanian.request.TeamRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ContextConfiguration(classes = LearnromanianRestApplication.class)
public class TeamStepDefinitions extends Util {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @When("user {word} sends POST team creation request with {} and {}")
    public void user_sends_team_creation_request(String userEmail, String teamName, String teamDescription) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/team/create")
                        .header("authorization", "Bearer ".concat(testContext().getBearerToken().toString()))
                        .content(objectMapper.writeValueAsString(
                                TeamRequest.builder()
                                        .name(teamName)
                                        .description(teamDescription)
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        testContext().setResponseCode(response.getResponse().getStatus());

        log.info("New {} team was created: {}.", teamName, teamRepository.findAll());
    }

    @When("user {word} sends POST remove student request for {word} from {}")
    public void adminUserSubmitsPOSTTeamRequestForRemovingTheUserFromTheTeam(String user, String studentEmail, String teamName) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/team/remove/{username}/{teamName}", studentEmail, teamName)
                        .header("authorization", "Bearer ".concat(testContext().getBearerToken().toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        testContext().setResponseCode(response.getResponse().getStatus());

        log.info("User {} war deleted from the {} team by {}.", studentEmail, teamName, user);
    }

    @And("user {word} sends POST add student request for {} to {}")
    public void userIsAddedToTheTeam(String user, String studentEmail, String teamName) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/team/addStudent/{username}/{teamName}", studentEmail, teamName)
                        .header("authorization", "Bearer ".concat(testContext().getBearerToken().toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        testContext().setResponseCode(response.getResponse().getStatus());

        log.info("Student with the Family Name : {} and First Name: {} was added to the team  {} ",
                userRepository.findByUserEmail(studentEmail).get().getUserFamilyName(),
                userRepository.findByUserEmail(studentEmail).get().getUserFirstName(),teamName);
    }

    @Then("new team with {}, {} is created")
    public void newTeamWithIsCreatedAndTeamHeadIs(String teamName, String teamDescription) {
        Team teamCreated = teamRepository.findByName(teamName);
        assertThat(teamCreated.getDescription()).isEqualTo(teamDescription);

        log.info("Assertion passed! Team {} with description {} created.", teamCreated.getName(), teamCreated.getDescription());
    }

    @Then("user {word} is removed from the team {}")
    public void userIsRemovedFromTheTeam(String studentEmail, String teamName) {
        Team team = teamRepository.findByName(teamName);
        User user = userRepository.findByUserEmail(studentEmail).get();
        team.getStudents().contains(user);
        assertThat(team.getStudents().contains(user)).isFalse();

        log.info("Assertions passed.");
    }

    @After
    public void cleanUp() {
        testContext().reset();
        teamRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        log.info("Deleted all users and roles from DB.");
    }
}