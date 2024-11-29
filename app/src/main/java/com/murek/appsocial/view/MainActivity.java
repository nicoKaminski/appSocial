package com.murek.appsocial.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.murek.appsocial.databinding.ActivityMainBinding;
import com.murek.appsocial.util.Validaciones;
import com.murek.appsocial.viewModel.MainViewModel;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        manejarEventos();
    }

    private void manejarEventos() {
        // Boton registrar
        binding.tvRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        // Boton iniciar sesion
        binding.btIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etMail.getText().toString().trim();
                String password = binding.etPass.getText().toString().trim();

                if (!Validaciones.validarEmail(email)) {
                    showToast("Email inválido");
                    return;
                }

                if (!Validaciones.controlarPassword(password)) {
                    showToast("La contraseña es inválida");
                    return;
                }

                // observar el resultado del login
                viewModel.login(email, password).observe(MainActivity.this, logingOk -> {
                    if (logingOk != null && logingOk) {
                        //startActivity(new Intent(MainActivity.this, UserActivity.class));
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        showToast("Error al iniciar sesión");
                    }
                });
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        limpiarCampos();
    }

    private void limpiarCampos() {
        binding.etMail.setText("");
        binding.etPass.setText("");
    }
}