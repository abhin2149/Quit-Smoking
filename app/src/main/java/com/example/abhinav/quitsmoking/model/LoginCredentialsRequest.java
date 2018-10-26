package com.example.abhinav.quitsmoking.model;

public class LoginCredentialsRequest {

    final String username;
    final String password;

    public LoginCredentialsRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
