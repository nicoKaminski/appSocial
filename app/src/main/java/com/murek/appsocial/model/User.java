package com.murek.appsocial.model;

public class User {
    private String userId;
    private String userName;
    private String userEmail;
    private String userpassword;
    private String userFotoPerfil;
    private String[] intereses;

    public User() {
    }

    public User(String userName, String userEmail, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userpassword = userPassword;
    }

    public User(String userId, String userName, String userEmail, String userpassword) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userpassword = userpassword;
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

    public String getUserFotoPerfil() {
        return userFotoPerfil;
    }

    public void setUserFotoPerfil(String userFotoPerfil) {
        this.userFotoPerfil = userFotoPerfil;
    }

    public String[] getIntereses() {
        return intereses;
    }

    public void setIntereses(String intereses) {
        this.intereses = new String[]{intereses};
    }
}