package com.murek.appsocial.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.murek.appsocial.providers.AuthProvider;

public class MainViewModel extends ViewModel {
    private final AuthProvider authProvider;

    public MainViewModel() {
        authProvider = new AuthProvider();
    }

    public LiveData<Boolean> login(String email, String password) {
        MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
        authProvider.singIn(email, password).observeForever(userId -> {
            loginResult.setValue(userId != null);
        });
        return loginResult;
    }
}
