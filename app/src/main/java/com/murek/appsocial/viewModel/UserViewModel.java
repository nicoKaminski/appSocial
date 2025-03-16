package com.murek.appsocial.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.murek.appsocial.model.User;
import com.murek.appsocial.providers.UserProvider;

import java.util.List;

public class UserViewModel extends ViewModel {

    private final UserProvider userProvider;
    private LiveData<User> currentUser;

    public UserViewModel() {
        userProvider = new UserProvider();
    }

    public LiveData<User> getCurrentUser() {
        if (currentUser == null) {
            currentUser = userProvider.getCurrentUser();
        }
        return currentUser;
    }

    public LiveData<Boolean> updateUser(User user) {
        return userProvider.updateUser(user);
    }

    public LiveData<User> getUserById(String userId) {
        return userProvider.getUserById(userId);
    }

    public LiveData<List<User>> getAllUsers() {
        return userProvider.getAllUsers();
    }

}
