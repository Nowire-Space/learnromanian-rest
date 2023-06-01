package nowire.space.learnromanian.stepdefinitions;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.inject.Inject;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.request.RegistrationRequest;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import nowire.space.learnromanian.controller.AccountController;
import nowire.space.learnromanian.model.User;

import java.io.IOException;
import java.io.InputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
public class RegistrationStepDefinitions {

    @Inject
    @MockBean
    private AccountController accountController;

    WireMockServer mockServer;

    @Before
    private void setUp() {
        mockServer = new WireMockServer();
        mockServer.start();
        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/learnromanian/v0.0.1/api/account/empty"))
                .willReturn(
                        aResponse()
                                .withBody(String.valueOf(new RegistrationRequest()))
                                .withStatus(HttpStatus.OK.value())));
    }

    @Given("^user with following (.*), (.*), (.*), (.*), (.*) and (.*)$")
    public void user_with_doe_and_john(String userFamilyName, String userLastName, String phoneNumber, String email,
                                       String password, String passwordCheck) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/learnromanian/v0.0.1/api/account/empty");
        HttpResponse httpResponse = httpClient.execute(request);
        String stringResponse = httpResponse.toString();

//        User newUser = new User();
//        newUser.setUserFamilyName(lastName);
//        newUser.setUserFirstName(firstName);
//        when(accountController.createAccount(any())).thenReturn(new ResponseEntity<>("OK", HttpStatus.OK));
//        log.info("First Name: {}. Last Name: {}", firstName, lastName);
        log.info("Response: {}", stringResponse);
    }

}
