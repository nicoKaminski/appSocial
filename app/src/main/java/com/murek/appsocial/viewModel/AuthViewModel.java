package com.murek.appsocial.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.murek.appsocial.providers.AuthProvider;

public class AuthViewModel extends ViewModel {

    private AuthProvider authProvider;

    public AuthViewModel() {
        this.authProvider = new AuthProvider();
    }

    public LiveData<Boolean> logOut() {
        return authProvider.logOut();
    }

}
