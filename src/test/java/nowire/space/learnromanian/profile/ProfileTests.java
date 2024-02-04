package nowire.space.learnromanian.profile;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/profile",
        glue = {"nowire.space.learnromanian.profile.stepdefinitions",
                "nowire.space.learnromanian.configuration"},
        plugin = {"pretty"}
)
public class ProfileTests {
}
