package com.murek.appsocial.providers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.murek.appsocial.model.User;

import java.lang.invoke.MutableCallSite;

public class UserProvider {
    private final FirebaseFirestore firestore;
    private static final String USER_COLLECTION = "users";

    private final FirebaseAuth auth;
    private static final String USER_COLLECTION2 = "users";

    public UserProvider() {
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public LiveData<String> createUser(User user) {
        MutableLiveData<String> result = new MutableLiveData<>();
        firestore.collection(USER_COLLECTION).document(user.getUserId()).set(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                result.setValue("Usuario creado correctamente");
            } else {
                result.setValue("Error al crear usuario");
            }
        });
        return result;
    }

    //otro metodo para obtener el usuario GET2
    public LiveData<User> getUser2() {
        MutableLiveData<User> userData = new MutableLiveData<>();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null) {
            User user = new User();
            user.setUserId(firebaseUser.getUid());
            user.setUserEmail(firebaseUser.getEmail());
            user.setUserName(firebaseUser.getDisplayName());

            userData.setValue(user);
        } else {
            Log.d("UserProvider", "No hay usuario autenticado");
            userData.setValue(null);
        }

        return userData;
    }

    //metodo ORIGINAL para obtener usuario
    public LiveData<User> getUser(String mail) {
        MutableLiveData<User> userData = new MutableLiveData<>();
        firestore.collection(USER_COLLECTION).whereEqualTo("userEmail", mail).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                User user = document.toObject(User.class);
                if (user != null) {
                    Log.d("UserProvider", "Usuario encontrado: " + user.getUserEmail());
                } else {
                    Log.d("UserProvider", "Error: Usuario no encontrado");
                }
                userData.setValue(user!=null? user : null);
            } else {
                Log.d("UserProvider", "Error: Usuario no encontrado en Firestore");
                userData.setValue(null);
            }
        }).addOnFailureListener(e -> {
            Log.d("UserProvider", "Error en consulta de Firestore: ", e);
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

    public LiveData<String> deleteUser(String userId) {
        MutableLiveData<String> result = new MutableLiveData<>();
        firestore.collection(USER_COLLECTION).document(userId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                result.setValue("Usuario eliminado correctamente");
            } else {
                result.setValue("Error al eliminar usuario");
            }
        });
        return result;
    }

}
