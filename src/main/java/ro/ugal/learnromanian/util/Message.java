package ro.ugal.learnromanian.util;

public class Message {
    public static String USER_NOT_FOUND = "User was not found";

    public static String userByIdNotFound(Integer id){
        return "User with id " + id + " was not found!";
    }

    public static String userByIdFound(Integer id){
        return "User with id " + id + " was successfully found.";
    }
}
