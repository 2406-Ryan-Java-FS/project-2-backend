package com.revature.models.dto;



public class SignUpOutput {
    private boolean signUpStatus;
    private String signUpStatusMessage;

    // Parameterized constructor
    public SignUpOutput(boolean signUpStatus, String signUpStatusMessage) {
        this.signUpStatus = signUpStatus;
        this.signUpStatusMessage = signUpStatusMessage;
    }

    // Getters and Setters
    public boolean isSignUpStatus() {
        return signUpStatus;
    }

    public void setSignUpStatus(boolean signUpStatus) {
        this.signUpStatus = signUpStatus;
    }

    public String getSignUpStatusMessage() {
        return signUpStatusMessage;
    }

    public void setSignUpStatusMessage(String signUpStatusMessage) {
        this.signUpStatusMessage = signUpStatusMessage;
    }
}