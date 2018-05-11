package com.example.richardantonios.testapp.models;

import android.arch.lifecycle.ViewModel;

public class UserModel extends ViewModel {

    private String username;
    private String password;
    private boolean rememberme;

    public UserModel() {
    }

    public UserModel(String username, String password, boolean rememberme) {
        this.username = username;
        this.password = password;
        this.rememberme = rememberme;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberme() {
        return rememberme;
    }

    public void setRememberme(boolean rememberme) {
        this.rememberme = rememberme;
    }
}
