package nowire.space.learnromanian.configuration;

import io.cucumber.java.DataTableType;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.LearnromanianRestApplication;
import nowire.space.learnromanian.model.Role;
import nowire.space.learnromanian.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ContextConfiguration(classes = LearnromanianRestApplication.class)
public class CucumberConfiguration {

    @Autowired
    private PasswordEncoder encoder;

    @DataTableType
    public Role mapRowToRole(Map<String, String> entry) {
        return Role.builder()
                .roleId(Integer.valueOf(entry.get("Id")))
                .roleName(entry.get("Role"))
                .build();
    }

    @DataTableType
    public User mapRowToUser(Map<String, String> entry) {
        return User.builder()
                .userFamilyName(entry.get("Family Name"))
                .userFirstName(entry.get("First Name"))
                .userPhoneNumber(entry.get("Phone Number"))
                .userEmail(entry.get("Email"))
                .userPassword(encoder.encode(entry.get("Password")))
                .userEnabled(optional(entry, "Enabled"))
                .userActivated(optional(entry, "Activated"))
                .build();
    }

    private Boolean optional(Map<String, String> entry, String column) {
        if (entry.get(column) == null)
            return true;
        return !entry.get(column).equalsIgnoreCase("false");
    }
}
