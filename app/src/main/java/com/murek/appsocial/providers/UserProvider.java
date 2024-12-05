package com.murek.appsocial.providers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.murek.appsocial.model.User;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class UserProvider {

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

    // actualizar un usuario en la base de datos de Parse
    public LiveData<String> updateUser(User user) {
        MutableLiveData<String> authResult = new MutableLiveData<>();

        //Obtener Usuario
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("objectId", user.getUserId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    ParseObject userObject = objects.get(0);
                    userObject.put("userName", user.getUserName());
                    userObject.put("userEmail", user.getUserEmail());
                    userObject.put("userPassword", user.getUserpassword());

                    //Guardar Usuario
                    userObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                authResult.setValue("Usuario actualizado correctamente");
                            } else {
                                authResult.setValue("Error al actualizar usuario: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    authResult.setValue("Error al obtener usuario: " + e.getMessage());
                }
            }
        });
        return authResult;
    }

    // eliminar un usuario de la base de datos de Parse
    public LiveData<String> deleteUser(User user) {
        MutableLiveData<String> authResult = new MutableLiveData<>();

        //Obtener Usuario
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("objectId", user.getUserId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    ParseObject userObject = objects.get(0);

                    //Eliminar Usuario
                    userObject.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                authResult.setValue("Usuario eliminado correctamente");
                            } else {
                                authResult.setValue("Error al eliminar usuario: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    authResult.setValue("Error al obtener usuario: " + e.getMessage());
                }
            }
        });
        return authResult;
    }

}
