package com.mealky.rest.model;

public enum ApiResponse {

    ACCOUNT_HAS_BEEN_ACTIVATED("Your account has been activated successfully!"),
    RESET_LINK_SENT("Link to reset your password has been sent to your email address."),
    NEW_PASSWORD_SET("New password has been set.");

    private String response;

    ApiResponse(String response) {
        this.response = response;
    }

    public String response() {
        return response;
    }
}