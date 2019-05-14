package com.mealky.rest.model;

public enum ApiError {

    CONFIRM_EMAIL("This account is not confirmed."),
    INVALID_TOKEN("Invalid token."),
    NO_SUCH_USER("User with this email does not exist."),
    WRONG_PASSWORD("Wrong password."),

    EMAIL_TAKEN("An account with this email already exists."),
    USERNAME_TAKEN("An account with this username already exists."),

    INVALID_EMAIL("This email is invalid."),
    INVALID_USERNAME("Username should contain only alphanumerics."),
    INVALID_PASSWORD("Password length should be longer than 5."),

    PASSWORDS_DOES_NOT_MATCH("New passwords do not matches or are too short."),
    EMAILS_DOES_NOT_MATCH("Emails does not match."),

    MEAL_NAME_EMPTY("Meal name cannot be empty."),
	MEAL_PREP_TIME("Meal preparation time cannot be lower than 1."),
	MEAL_DESCRIPTION("Meal description cannot be empty."),
	MEAL_INGREDIENT("A meal should contain at least one ingredient."),
	MEAL_IMAGE("A meal should contain at least one and a maximum of five images."),
    MEAL_USER("This user does not exist."),
    SOMETHING_WENT_WRONG("Something went wrong.");

    private String error;

    ApiError(String error) {
        this.error = error;
    }

    public String error() {
        return error;
    }
}