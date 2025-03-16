package com.murek.appsocial.providers;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.murek.appsocial.R;
import com.murek.appsocial.model.User;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class AuthProvider {

    public AuthProvider() {}

    // Logeo con Parse
    public LiveData<String> singIn(String email, String password) {
        MutableLiveData<String> authResult = new MutableLiveData<>();
        // para saber si la autenticacion fue exitosa
        ParseUser.logInInBackground(email, password, (user, e) -> {
                if (e == null) {
                    authResult.setValue(user.getObjectId());
                    Log.d("AuthProvider", "Login exitoso: " + user.getObjectId());
                } else {
                    authResult.setValue(null);
                    Log.d("AuthProvider", "Login fallido: " + e.getMessage());
                }
        });
        return authResult;
    }

    // Registro con Parse
    public LiveData<String> singUp(User user) {
        Log.d("AuthProvider", "Intentando registrar: " + user.getUserName() + ", " + user.getUserEmail());
        MutableLiveData<String> authResult = new MutableLiveData<>();
        //validar
        if (user.getUserName() == null || user.getUserEmail() == null || user.getUserpassword() == null) {
            Log.e("AuthProvider", "Uno o más valores son nulos: " +
                    "Username=" + user.getUserName() + ", " +
                    "Password=" + user.getUserpassword() + ", " +
                    "Email=" + user.getUserEmail());
            authResult.setValue(null);
            return authResult;
        }

//        ParseUser userParse = new ParseUser();
        User parseUser = new User();
        parseUser.setUsername(user.getUserName());
        parseUser.setEmail(user.getUserEmail());
        parseUser.setPassword(user.getUserpassword());
        user.signUpInBackground(e -> {
            if (e == null) {
                authResult.setValue("Registro exitoso");
                Log.d("AuthProvider", "Registro exitoso: " + parseUser.getObjectId());
            } else {
                authResult.setValue(null);
                Log.d("AuthProvider", "Registro fallido: " + e.getMessage());
            }
        });
        return authResult;
    }

    //Obtener el ID del usuario actual en Parse
    public LiveData<String> getCurrentUserId() {
        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            currentUserId.setValue(currentUser.getObjectId());
            Log.d("AuthProvider", "ID del usuario actual: " + currentUser.getObjectId());
        } else {
            Log.d("AuthProvider", "No hay un usuario autenticado");
        }
        return currentUserId;
    }

    //Cerrar sesion
    public LiveData<Boolean> logOut() {
        MutableLiveData<Boolean> logoutResult = new MutableLiveData<>();
        ParseUser.logOutInBackground(e -> {
            if (e == null) {
                logoutResult.setValue(true);
                Log.d("AuthProvider", "Caché eliminada y usuario desconectado.");
            } else {
                logoutResult.setValue(false);
                Log.e("AuthProvider", "Error al desconectar al usuario: ", e);
            }
        });
        return logoutResult; }
}


