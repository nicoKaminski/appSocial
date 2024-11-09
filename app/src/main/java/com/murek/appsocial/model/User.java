package com.murek.appsocial.model;

public class User {
    private String userId;
    private String userName;
    private String userEmail;
    private String userpassword;

    public User() {
    }

    public User(String userName, String userEmail, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userpassword = userPassword;
    }

    public User(String id, String userName, String userEmail, String userPassword) {
        this.userId = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userpassword = userPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }
}