package com.mealky.rest.model;

public enum ApiError {

    CONFIRM_EMAIL("This account is not confirmed."),
    INVALID_TOKEN("Invalid token."),
    NO_SUCH_USER("User with this email does not exists."),
    WRONG_PASSWORD("Wrong password."),

    EMAIL_TAKEN("Account with this email already exists."),
    USERNAME_TAKEN("Account with this username already exists."),

    INVALID_EMAIL("This email is invalid."),
    INVALID_USERNAME("Username should contain only alphanumerics."),
    INVALID_PASSWORD("Password length should be longer than 5."),

    PASSWORDS_DOES_NOT_MATCH("New passwords do not matches or are too short."),
    EMAILS_DOES_NOT_MATCH("Emails does not match."),

    SOMETHING_WENT_WRONG("Something went wrong.");

    private String error;

    ApiError(String error) {
        this.error = error;
    }

    public String error() {
        return error;
    }
}
