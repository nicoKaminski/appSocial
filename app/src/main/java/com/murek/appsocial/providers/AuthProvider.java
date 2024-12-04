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

    public AuthProvider() {
    }

    public AuthProvider(Context context) {
        Parse.initialize(new Parse.Configuration.Builder(context)
                .applicationId(context.getString(R.string.back4app_app_id))
                .clientKey(context.getString(R.string.back4app_client_key))
                .server(context.getString(R.string.back4app_server_url))
                .build()
        );
    }

    // metodo
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

    //Logeo con Parse
    public LiveData<String> singUp(User user) {
        MutableLiveData<String> authResult = new MutableLiveData<>();
        ParseUser userParse = new ParseUser();
        userParse.setUsername(user.getUserName());
        userParse.setEmail(user.getUserEmail());
        userParse.setPassword(user.getUserpassword());
        userParse.signUpInBackground(e -> {
            if (e == null) {
                authResult.setValue(userParse.getObjectId());
                Log.d("AuthProvider", "Registro exitoso: " + userParse.getObjectId());
            } else {
                authResult.setValue(null);
                Log.d("AuthProvider", "Registro fallido: " + e.getMessage());
            }
        });
        return authResult;
    }

    //Logeo con Parse VIEJO
    /*
    public LiveData<String> singUp(String email, String password) {
        MutableLiveData<String> authResult = new MutableLiveData<>();
        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setPassword(password);
        user.signUpInBackground(e -> {
            if (e == null) {
                authResult.setValue(user.getObjectId());
                Log.d("AuthProvider", "Registro exitoso: " + user.getObjectId());
            } else {
                authResult.setValue(null);
                Log.d("AuthProvider", "Registro fallido: " + e.getMessage());
            }

        });
        return authResult;
    }
    */

    //Obtener el ID del usuario actual en Parse
    public LiveData<String> getCurrentUserId() {
        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            currentUserId.setValue(currentUser.getObjectId());
            Log.d("AuthProvider", "ID del usuario actual: " + currentUser.getObjectId());
        } else {
            Log.d("AuthProvider", "No hay un usuario autenticado");
//            currentUserId.setValue(null);
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


