package com.murek.appsocial.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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
        viewModel.getRegisterResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String result) {
                showToast(result);
            }
        });
        manejarEventos();
    }

    private void manejarEventos() {
        // Boton volver atras
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
                limpiar();
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

        /* CODIGO VIEJO
        User user = new User(usuario, email, password);
        viewModel.register(user).observe(this, result -> {
            showToast(result);
        });
        */

        User user = new User(usuario, email, password);
        viewModel.register(user);
        viewModel.getRegisterResult().observe(this, result -> {
            showToast(result);
        });
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
//        finish();
    }

    private void limpiar() {
        binding.etNewUsuario.setText("");
        binding.etNewMail.setText("");
        binding.etPassNew.setText("");
        binding.etPassNewConfirm.setText("");
    }
}