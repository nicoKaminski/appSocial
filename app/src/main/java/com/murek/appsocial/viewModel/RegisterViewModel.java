package com.murek.appsocial.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.murek.appsocial.model.User;
import com.murek.appsocial.providers.AuthProvider;
import com.parse.ParseUser;

public class RegisterViewModel extends ViewModel {
    private AuthProvider authProvider;
    private MutableLiveData<String> registerResult = new MutableLiveData<>();


    public RegisterViewModel() {
        this.authProvider = new AuthProvider();
    }

    public LiveData<String> getRegisterResult() {
        return registerResult;
    }

    /**
     * CODIGO VIEJO....
     * public void register(User user) {
     * ParseUser parseUser = new ParseUser();
     * parseUser.setUsername(user.getUserName());
     * parseUser.setEmail(user.getUserEmail());
     * parseUser.setPassword(user.getUserpassword());
     * parseUser.signUpInBackground(e -> {
     * if (e == null) {
     * registerResult.setValue(parseUser.getObjectId());
     * Log.d("AuthProvider", "Registro exitoso: " + parseUser.getObjectId());
     * } else {
     * registerResult.setValue(null);
     * Log.e("AuthProvider", "Error en el registro: " + e.getMessage());
     * }
     * });
     * }
     */

    public void register(User user) {
        LiveData<String> result = authProvider.singUp(user);

        result.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String objectId) {
                if (objectId != null) {

                    registerResult.setValue(objectId);
                    Log.d("RegisterViewModel", "Usuario registrado con ID: " + objectId);
                } else {
                    // Registration failed
                    registerResult.setValue(null);
                    Log.e("RegisterViewModel", "Error durante el registro.");
                }

                result.removeObserver(this);
            }
        });
    }

}

