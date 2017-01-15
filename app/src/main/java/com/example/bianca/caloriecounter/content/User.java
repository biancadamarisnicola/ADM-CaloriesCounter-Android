package com.example.bianca.caloriecounter.content;

/**
 * Created by bianca on 19.11.2016.
 */

public class User {
    private String mUsername;
    private String mPassword;
    private String mToken;

    public User(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    public User(String username, String password, String token) {
        mUsername = username;
        mPassword = password;
        mToken = token;
    }

    public User(Object o, Object o1, String token) {
        mToken = token;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "mUsername='" + mUsername + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mToken='" + mToken + '\'' +
                '}';
    }
}
