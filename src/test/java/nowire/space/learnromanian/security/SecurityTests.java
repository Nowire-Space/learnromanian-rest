package nowire.space.learnromanian.security;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/security",
        glue = {"nowire.space.learnromanian.security.stepdefinitions",
                "nowire.space.learnromanian.configuration"},
        plugin = {"pretty"}
)
public class SecurityTests {
}
