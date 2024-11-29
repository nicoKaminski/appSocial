package com.murek.appsocial.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.murek.appsocial.model.User;
import com.murek.appsocial.providers.AuthProvider;

public class RegisterViewModel extends ViewModel {
    private AuthProvider authProvider;
    private MutableLiveData<String> registerResult = new MutableLiveData<>();

    public RegisterViewModel() {
    }

    public LiveData<String> getRegisterResult() {
        return registerResult;
    }

    public void register(User user) {
        authProvider = new AuthProvider();
        authProvider.singUp(user).observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                registerResult.setValue(s);
            }
        });
    }
}

