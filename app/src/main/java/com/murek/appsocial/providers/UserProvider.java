package com.murek.appsocial.providers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.murek.appsocial.model.User;

import java.lang.invoke.MutableCallSite;

public class UserProvider {
    private final FirebaseFirestore firestore;
    private static final String USER_COLLECTION = "users";

    public UserProvider() {
        firestore = FirebaseFirestore.getInstance();
    }

    public LiveData<String> createUser(User user) {
        MutableLiveData<String> result = new MutableLiveData<>();
        firestore.collection(USER_COLLECTION).document(user.getUserId()).set(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                result.setValue("Usuario registrado correctamente");
            } else {
                result.setValue("Error al registrar usuario");
            }
        });
        return result;
    }

    public LiveData<User> getUser(String mail) {
        MutableLiveData<User> userData = new MutableLiveData<>();
        firestore.collection(USER_COLLECTION).whereEqualTo("email", mail).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                User user = document.toObject(User.class);
                if (user != null) {
                    Log.d("UserProvider", "Usuario encontrado: " + user.getUseremail());
                } else {
                    Log.d("UserProvider", "Error: Usuario no encontrado");
                }
                userData.setValue(user);
            } else {
                Log.d("UserProvider", "Error: Usuario no encontrado en Firestore");
                userData.setValue(null);
            }
        }).addOnFailureListener(e -> {
            Log.d("UserProvider", "Error en consulta de Firestore: " + e.getMessage());
            userData.setValue(null);
        });
        return userData;
    }

    public LiveData<String> updateUser(User user) {
        MutableLiveData<String> result = new MutableLiveData<>();
        firestore.collection(USER_COLLECTION).document(user.getUserId()).set(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                result.setValue("Usuario actualizado correctamente");
            } else {
                result.setValue("Error al actualizar usuario");
            }
        });
        return result;
    }

    public LiveData<String> deleteUser(User user) {
        MutableLiveData<String> result = new MutableLiveData<>();
        firestore.collection(USER_COLLECTION).document(user.getUserId()).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                result.setValue("Usuario eliminado correctamente");
            } else {
                result.setValue("Error al eliminar usuario");
            }
        });
        return result;
    }

}
