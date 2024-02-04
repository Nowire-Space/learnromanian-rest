package nowire.space.learnromanian.registration;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/registration",
        glue = {"nowire.space.learnromanian.registration.stepdefinitions",
                "nowire.space.learnromanian.configuration"},
        plugin = {"pretty"}
)
public class RegistrationTests {
}
