package com.ds.Helpers;

import com.ds.Entities.SystemUser;

public class UserSession {

    private static UserSession instance;

    private SystemUser currentUser;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void login(SystemUser user) {
        this.currentUser = user;
    }

    public SystemUser getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        this.currentUser = null;
         instance = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}