package nowire.space.learnromanian.util;

public class Message {
    public static String USER_AUTHENTICATION_TRUE = "User is authenticated.";
    public static String USER_AUTHENTICATION_FALSE = "User is NOT authenticated.";

    public static String USER_LOGIN_TRUE = "User was logged in.";
    public static String USER_LOGIN_FALSE = "User was NOT found.";

    public static String USER_REGISTRATION_TRUE = "User was successfully registered.";
    public static String USER_REGISTRATION_FALSE = "Email address is already registered.";
    public static String USER_REGISTRATION_ERROR = "An error occurred.";

    public static String USER_ACTIVATION_TRUE = "Your account was successfully activated.";

    public static String ADMIN_VALIDATION_ERROR = "This account is already enabled.";

    public static String EMAIL_VALIDATION_SUBJECT = "Your new LearnRomanian account is here!";

    public static String EMAIL_PASSWORD_RESET_SUBJECT = "Your new LearnRomanian account password is here!";

    public static String PASSWORD_RESET_TRUE = "Your new password is already in your email inbox!";


    public static String USER_REGISTRATION_TRUE(String firstName, String familyName, String email) {
        return "Account for ".concat(firstName).concat(" ").concat(familyName).concat(" with email ")
                .concat(email).concat(" was created.");
    }

    public static String ADMIN_VALIDATION_TRUE(String firstName, String familyName, String email) {
        return "Account for ".concat(firstName).concat(" ").concat(familyName).concat(" with email ")
                .concat(email).concat(" was enabled.");
    }

    public static String ADMIN_VALIDATION_USER_NOT_FOUND(Integer id) {
        return "User with id ".concat(id.toString()).concat(" was not found.");
    }

    public static String ADMIN_REJECT_TRUE(String id) {
        return "User with id ".concat(id).concat(" was successfully deleted.");
    }

    public static String ADMIN_REJECT_FALSE(String id) {
        return "User with id ".concat(id).concat(" was not found!");
    }

    public static String EMAIL_VALIDATION_TEXT(String firstName, String familyName) {
        return "Dear ".concat(firstName.concat(" ").concat(familyName)).concat(", welcome to LearnRomanian!");
    }

    public static String EMAIL_VALIDATION_HTML(String webappUrl, String token) {
        return "<form action='".concat(webappUrl).concat("validate/").concat(token).concat("'><h3>Please hit <input type='submit' value='The Button' /> to activate your account.</h3></form>");
    }

    public static String EMAIL_PASSWORD_RESET_HTML(String newPassword) {
        return "<h3>Please use new password to log into your account: ".concat(newPassword).concat("</h3>");
    }

}
