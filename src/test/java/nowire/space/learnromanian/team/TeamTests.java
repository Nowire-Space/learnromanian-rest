package nowire.space.learnromanian.team;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/team",
        glue = {"nowire.space.learnromanian.team.stepdefinitions",
                "nowire.space.learnromanian.configuration"},
        plugin = {"pretty"}
)
public class TeamTests {
}
