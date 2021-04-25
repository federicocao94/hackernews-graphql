package com.howtographql.hackernews.model;

public class AuthData {

    private String email;

    private String password;

    public AuthData() {
    }

    public AuthData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
