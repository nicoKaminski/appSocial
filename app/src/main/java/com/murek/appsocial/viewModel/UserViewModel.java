package com.murek.appsocial.viewModel;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.murek.appsocial.model.User;
import com.murek.appsocial.providers.AuthProvider;
import com.murek.appsocial.providers.UserProvider;

public class UserViewModel extends ViewModel {
    private final AuthProvider authProvider;
    private final UserProvider userProvider;
    private final MutableLiveData<User> currentUser;
    private final MutableLiveData<String> estado;

    public UserViewModel() {
        authProvider = new AuthProvider();
        userProvider = new UserProvider();
        currentUser = new MutableLiveData<>();
        estado = new MutableLiveData<>();
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public UserProvider getUserProvider() {
        return userProvider;
    }

    public MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }

    public MutableLiveData<String> getEstado() { //aca la profe lo llama getOperationStatus
        return estado;
    }


    public void createUser(User user, LifecycleOwner lifecycleOwner) {
        authProvider.singUp(user.getUserEmail(), user.getUserpassword()).observe(lifecycleOwner, sId -> {
            if (sId != null) {
                user.setUserId(sId);
                userProvider.createUser(user).observe(lifecycleOwner, status -> {
                    if (status != null) {
//                        estado.setValue(status);
                        estado.setValue("Usuario registrado correctamente");
                    } else {
                        estado.setValue("Error al crearar usuario en Firebase");
                    }
                });
            } else {
                estado.setValue("Error al registrar usuario en Firebase");
            }
        });
    }


    public void updateUser(User user, LifecycleOwner lifecycleOwner) {
        LiveData<String> result = userProvider.updateUser(user);
        result.observe(lifecycleOwner, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                estado.setValue(result.getValue());
            }
        });
    }

    public void deleteUser(String userId, LifecycleOwner lifecycleOwner) {
        LiveData<String> result = userProvider.deleteUser(userId);
        result.observe(lifecycleOwner, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                estado.setValue(result.getValue());
            }
        });
    }

    public void getUser(String email, LifecycleOwner lifecycleOwner) {
        LiveData<User> user = userProvider.getUser(email);
        user.observe(lifecycleOwner, new Observer<User>() {
            @Override
            public void onChanged(User foundUser) {
                if (foundUser != null) {
                    Log.d("User info ", "ID: " + foundUser.getUserId() + " - Nombre: " + foundUser.getUserName());
                    currentUser.setValue(foundUser);
                } else {
                    estado.setValue("Error al obtener el usuario");
                }
            }
        });
    }

    // otro metodo para ger user
    public void getUser2() {
        LiveData<User> user = userProvider.getUser2();
        user.observeForever(foundUser -> {
            if (foundUser != null) {
                currentUser.setValue(foundUser);
                Log.d("UserViewModel", "Usuario autenticado: " + foundUser.getUserEmail());
            } else {
                estado.setValue("No se encontró ningún usuario autenticado");
            }
        });
    }
}
