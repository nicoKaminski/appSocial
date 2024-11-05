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
                }
            }
        });
    }

    private void manejarEventos() {

        binding.btGestionCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User usuario = obtenerDatosUsuario();
                viewModel.createUser(usuario, UserActivity.this);
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
        binding.etGestionMail.setText(user.getUseremail());
        binding.etGestionPass.setText(user.getUserpassword());
        Log.d("mostrar", user.getUserId()+" - "+user.getUserName());
    }

    private void limpiar() {
        binding.etGestionId.setText("");
        binding.etGestionUsuario.setText("");
        binding.etGestionMail.setText("");
        binding.etGestionPass.setText("");
    }

}
