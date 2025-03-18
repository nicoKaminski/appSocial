package com.murek.appsocial.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("_User")
    public class User extends ParseUser {

    public User() {
    }

    public String getUserId() {
        return getObjectId();
    }

    public void setUserId(String userId) {
        put("objectId", userId);
    }

    public String getUserName() {
        return getString("username");
    }

    public void setUserName(String userName) {
        put("username", userName);
    }

    public String getUserEmail() {
        return getString("email");
    }

    public void setUserEmail(String userEmail) {
        if (userEmail != null) {
            put("email", userEmail);
        } else {
            Log.w("User", "El correo electrónico es nulo.");
        }
    }

    public String getUserpassword() {
        return getString("password");
    }

    public void setUserpassword(String userpassword) {
        put("password", userpassword);
    }

    public String getUserFotoPerfil() {
        return getString("foto_perfil");
    }

    public void setUserFotoPerfil(String userFotoPerfil) {
        put("foto_perfil", userFotoPerfil);
    }

    public String getRedSocial() {
        return getString("red_social");
    }

    public void setRedSocial(String redSocial) {
        if (redSocial != null) {
            put("red_social", redSocial);
        }
    }
}