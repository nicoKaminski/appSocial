package com.murek.appsocial.providers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthProvider {
    private FirebaseAuth firebaseAuth;

    // constructor
    public AuthProvider() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    // metodo
    private LiveData<String> helperAuth(Task<AuthResult> task) {
        MutableLiveData<String> authResult = new MutableLiveData<>();
        // para saber si la autenticacion fue exitosa
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                    authResult.setValue(firebaseAuth.getCurrentUser().getUid());
                } else {
                    if (task.getException() != null) {
                        Log.e("AuthProvider", "Error de autenticación", task.getException());
                            authResult.setValue(null);
                        }
                    }
                }
        });
        return authResult;
    }

    // Iniciar Sesión en Firebase
    public LiveData<String> singIn(String email, String password) {
        return helperAuth(firebaseAuth.signInWithEmailAndPassword(email, password));
    }

    // Crear Cuenta en Firebase
    public LiveData<String> singUp(String email, String password) {
        return helperAuth(firebaseAuth.createUserWithEmailAndPassword(email, password));
    }
    public String getCurrentUserId() {
        return firebaseAuth.getCurrentUser() != null? firebaseAuth.getCurrentUser().getUid():null;
    }
}
