package com.murek.appsocial.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.murek.appsocial.viewModel.UserViewModel;
import com.murek.appsocial.databinding.ActivityUserBinding;
import com.murek.appsocial.model.User;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private UserViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        esperarObservers();
        manejarEventos();
    }

    private void esperarObservers() {
        viewModel.getEstado().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                Toast.makeText(UserActivity.this, status, Toast.LENGTH_SHORT).show();
                limpiar();
            }
        });
        viewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    mostrarUsuario(user);
                } else {
                    Log.e("UserActivity", "El usuario es null");
                }
            }
        });
    }

    private void manejarEventos() {

        // Boton crear vista gestion
        binding.btGestionCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User usuario = obtenerDatosUsuario();
                viewModel.createUser(usuario, UserActivity.this);
            }
        });

        // Boton actualizar
        binding.btGestionActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User usuario = obtenerDatosUsuario();
                viewModel.updateUser(usuario, UserActivity.this);
            }
        });

        // Boton borrar
        binding.btGestionEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = binding.etGestionId.getText().toString().trim();
                viewModel.deleteUser(id, UserActivity.this);
            }
        });

        // Boton leer
        binding.btGestionLeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = binding.etGestionMail.getText().toString().trim();
                viewModel.getUser(mail, UserActivity.this);
            }
        });

        //Boton atras
        binding.circuloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private User obtenerDatosUsuario() {
        String id = binding.etGestionId.getText().toString().trim();
        String userName = binding.etGestionUsuario.getText().toString().trim();
        String email = binding.etGestionMail.getText().toString().trim();
        String password = binding.etGestionPass.getText().toString().trim();
        return new User(id, userName, email, password);
    }

    private void mostrarUsuario(User user) {
        binding.etGestionId.setText(user.getUserId());
        binding.etGestionUsuario.setText(user.getUserName());
        binding.etGestionMail.setText(user.getUserEmail());
        binding.etGestionPass.setText(user.getUserpassword());
        Log.d("mostrar", user.getUserId() + " - " + user.getUserName());
    }

    private void limpiar() {
        binding.etGestionId.setText("");
        binding.etGestionUsuario.setText("");
        binding.etGestionMail.setText("");
        binding.etGestionPass.setText("");
    }

}
