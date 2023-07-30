package nowire.space.learnromanian.service;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nowire.space.learnromanian.util.Message;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${email.api.key}")
    private String emailApiKey;

    @Value("${email.api.secret}")
    private String emailApiSecret;

    @Value("${email.from.address}")
    private String emailSender;

    @Value("${email.from.name}")
    private String emailSenderName;

    @Value("${webapp.url}")
    private String appUrl;

    public void sendValidationEmail(String userEmail, String firstName, String familyName, String token) throws MailjetException {
        MailjetClient client = new MailjetClient(
                ClientOptions.builder()
                        .apiKey(emailApiKey)
                        .apiSecretKey(emailApiSecret)
                        .build()
        );
        MailjetRequest request = new MailjetRequest(Email.resource)
                .property(Email.FROMEMAIL, emailSender)
                .property(Email.FROMNAME, emailSenderName)
                .property(Email.SUBJECT, Message.EMAIL_VALIDATION_SUBJECT)
                .property(Email.HTMLPART, Message.EMAIL_VALIDATION_HTML(appUrl, token))
                .property(Email.RECIPIENTS, new JSONArray().put(new JSONObject().put(Email.EMAIL, userEmail)));
        MailjetResponse response = client.post(request);
        log.info("Authentication token for user {} is {}", userEmail, token);
        log.info("MailJet sender response code: {} and data: {}", response.getStatus(), response.getData().toString());
    }
}
