package com.traveloka_project.traveloka.util;

public class ErrorMessage {
    public static String EMAIL_OR_PASSWORD_ERROR = "Email or password is not correct";
    public static String SIGN_IN_AGAIN = "Sign in again";
    public static String generateNotFoundMessage(String subject){
        StringBuilder stringBuilder = new StringBuilder("Not found ");
        stringBuilder.append(subject).append(" as: ");
        return stringBuilder.toString();
    }
}
