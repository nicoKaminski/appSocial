package com.murek.appsocial.view;

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
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        manejarEventos();
    }

    private void manejarEventos() {
        // Evento volver atras
        binding.circuloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
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
        String usuario = binding.etUsuario.getText().toString().trim();
        String email = binding.etMail.getText().toString().trim();
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

        User user = new User(usuario, email, password);
        viewModel.register(user).observe(this, result -> {
            showToast(result);
        });
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
        finish();
    }
}