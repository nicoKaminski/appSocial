package com.murek.appsocial.providers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.murek.appsocial.model.User;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class UserProvider {

    public UserProvider() {}

    //crea un nuevo usuario en la base de datos de Parse
    public LiveData<String> createUser(User user) {
        MutableLiveData<String> authResult = new MutableLiveData<>();
        ParseObject newUser = new ParseObject("User");
        newUser.put("userName", user.getUserName());
        newUser.put("userEmail", user.getUserEmail());
        newUser.put("userPassword", user.getUserpassword());

        newUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    authResult.setValue("Usuario creado correctamente");
                } else {
                    authResult.setValue("Error al crear usuario: " + e.getMessage());
                }
            }
        });
        return authResult;
    }

    // obtener un usuario por su correo electrónico en Parse
    public LiveData<User> getUserByEmail(String email) {
        MutableLiveData<User> userData = new MutableLiveData<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("userEmail", email);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    ParseObject userObject = objects.get(0);
                    User user = new User();
                    user.setUserId(userObject.getObjectId());
                    user.setUserName(userObject.getString("userName"));
                    user.setUserEmail(userObject.getString("userEmail"));
                    user.setUserpassword(userObject.getString("userPassword"));

                    userData.setValue(user);
                } else {
                    userData.setValue(null);
                }
            }
        });
        return userData;
    }

    // Método para obtener el usuario
    public LiveData<User> getCurrentUser() {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser != null) {
            User user = (User) parseUser;
            userLiveData.setValue(user); // Establece el usuario en LiveData
            Log.d("UserProvider", "Usuario actual obtenido: " + user.getObjectId());
        } else {
            userLiveData.setValue(null); // No hay usuario autenticado
            Log.e("UserProvider", "No hay usuario autenticado.");
        }
        return userLiveData;
    }

    // Método para actualizar un usuario
    public LiveData<Boolean> updateUser(User user) {
        MutableLiveData<Boolean> updateResult = new MutableLiveData<>();

       /*
       user.saveInBackground(e -> {
            if (e == null) {
                updateResult.setValue(true);
                Log.d("UserProvider", "Usuario actualizado: " + user.getObjectId());
            } else {
                updateResult.setValue(false);
                Log.e("UserProvider", "Error actualizando usuario", e);
            }
        });
        */

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    updateResult.setValue(true);
                    Log.d("UserProvider", "Usuario actualizado exitosamente: " + user.getObjectId());
                } else {
                    updateResult.setValue(false);
                    Log.e("UserProvider", "Error actualizando usuario: ", e);
                }
            }
        });
        return updateResult;
    }

    // Método para obtener un usuario por ID
    public LiveData<User> getUserById(String userId) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.getInBackground(userId, (user, e) -> {
            if (e == null) {
                userLiveData.setValue(user);
                Log.d("UserProvider", "Usuario obtenido: " + user.getObjectId());
            } else {
                userLiveData.setValue(null);
                Log.e("UserProvider", "Error al obtener usuario: ", e);
            }
        });
        return userLiveData;
    }

    // Método para obtener todos los usuarios
    public LiveData<List<User>> getAllUsers() {
        MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> users, ParseException e) {
                if (e == null) {
                    usersLiveData.setValue(users);
                    Log.d("UserProvider", "Usuarios obtenidos: " + users.size());
                } else {
                    usersLiveData.setValue(null);
                    Log.e("UserProvider", "Error al obtener usuarios: ", e);
                }
            }
        });
        return usersLiveData;
    }

    // eliminar un usuario de la base de datos de Parse
    public LiveData<Boolean> deleteUser(User user) {
        MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();
        user.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    deleteResult.setValue(true);
                    Log.d("UserProvider", "Usuario eliminado exitosamente: " + user.getObjectId());
                } else {
                    deleteResult.setValue(false);
                    Log.e("UserProvider", "Error eliminando usuario: ", e);
                }
            }
        });
        return deleteResult;
    }


}
