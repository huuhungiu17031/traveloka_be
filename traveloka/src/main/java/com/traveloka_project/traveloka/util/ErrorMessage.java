package com.traveloka_project.traveloka.util;

public class ErrorMessage {
    public static final String EMAIL_OR_PASSWORD_ERROR = "Email or password is not correct";
    public static final String SIGN_IN_AGAIN = "Sign in again";
    public static final String INVALID_CHARATER = "Filename contains invalid path sequence ";
    public static final String FILE_EXCEED_LIMIT = "File size exceeds the limit (10MB)";
    public static final String FILE_NOT_FOUND = "File not found with id: ";

    public static String generateNotFoundMessage(String subject) {
        StringBuilder stringBuilder = new StringBuilder("Not found ");
        stringBuilder.append(subject).append(" as: ");
        return stringBuilder.toString();
    }
}
