package com.murek.appsocial.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.murek.appsocial.databinding.ActivityRegistrerBinding;
import com.murek.appsocial.model.User;
import com.murek.appsocial.util.Validaciones;
import com.murek.appsocial.viewModel.RegisterViewModel;


public class RegisterActivity extends AppCompatActivity {

    private ActivityRegistrerBinding binding;
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        manejarEventos();
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        // Observa los resultados del registro una sola vez
        viewModel.getRegisterResult().observe(this, result -> {
            if (result != null) {
                showToast("Usuario registrado exitosamente con ID: " + result);
                limpiar();
            } else {
                showToast("Error durante el registro. Por favor, inténtelo de nuevo.");
            }
        });

    }

    private void manejarEventos() {
        binding.circuloBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Evento registrar
        binding.btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarRegistro();
            }
        });
    }

    private void realizarRegistro() {
        String usuario = binding.etNewUsuario.getText().toString().trim();
        String email = binding.etNewMail.getText().toString().trim();
        String password = binding.etPassNew.getText().toString().trim();
        String passwordConfirm = binding.etPassNewConfirm.getText().toString().trim();

        if (!Validaciones.validarUsuario(usuario)) {
            showToast("El nombre de usuario es inválido");
            return;
        }

        if (!Validaciones.validarEmail(email)) {
            showToast("Email inválido");
            return;
        }

        String passError = Validaciones.validarPassword(password, passwordConfirm);
        if (passError != null) {
            showToast(passError);
            return;
        }

        User user = new User();
        user.setUserName(usuario);
        user.setUserEmail(email);
        user.setUserpassword(password);
        viewModel.register(user);
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void limpiar() {
        binding.etNewUsuario.setText("");
        binding.etNewMail.setText("");
        binding.etPassNew.setText("");
        binding.etPassNewConfirm.setText("");
    }
}