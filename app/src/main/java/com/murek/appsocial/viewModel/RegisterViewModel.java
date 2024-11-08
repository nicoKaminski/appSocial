package com.murek.appsocial.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.murek.appsocial.model.User;
import com.murek.appsocial.providers.AuthProvider;
import com.murek.appsocial.providers.UserProvider;

public class RegisterViewModel extends ViewModel {
    private final AuthProvider authProvider;
    private final UserProvider userProvider;

    public RegisterViewModel() {
        authProvider = new AuthProvider();
        userProvider = new UserProvider();
    }

    public LiveData<String> register(User user) {
        MutableLiveData<String> registerResult = new MutableLiveData<>();

        //primero registra el usuario en firebase
        authProvider.singUp(user.getUserEmail(), user.getUserpassword()).observeForever(new Observer<String>() {
            @Override
            public void onChanged(String sId) {
                if (sId != null) {
                    user.setUserId(sId);
                    userProvider.createUser(user).observeForever(new Observer<String>() {
                        @Override
                        public void onChanged(String result) {
                            registerResult.setValue(result);
                        }
                    });
                } else {
                    registerResult.setValue("Error al registrar usuario");
                }
            }
        });
        return registerResult;
    }
}
