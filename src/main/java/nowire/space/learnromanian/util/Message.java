package nowire.space.learnromanian.util;

public class Message {
    public static String USER_AUTHENTICATION_TRUE = "User is authenticated.";
    public static String USER_AUTHENTICATION_FALSE = "User is NOT authenticated.";

    public static String USER_LOGIN_TRUE = "User was logged in.";
    public static String USER_LOGIN_FALSE = "User was NOT found.";

    public static String USER_REGISTRATION_TRUE = "User was successfully registered.";
    public static String USER_REGISTRATION_FALSE = "Email address is already registered.";
    public static String USER_REGISTRATION_ERROR = "An error occurred.";

    public static String ADMIN_VALIDATION_ERROR = "This account is already enabled.";

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
}
