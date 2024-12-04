package com.murek.appsocial.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.murek.appsocial.adapters.ImageAdapter;
import com.murek.appsocial.databinding.ActivityPostBinding;
import com.murek.appsocial.model.Post;
import com.murek.appsocial.util.ImageUtils;
import com.murek.appsocial.util.Validaciones;
import com.murek.appsocial.viewModel.PostViewModel;
import java.util.ArrayList;
import java.util.List;


public class PostActivity extends AppCompatActivity {

    private ActivityPostBinding binding;
    private PostViewModel viewModel;
    private static final int MAX_IMAGES = 3;
    private final int REQUEST_IMAGE = 1;
    private final List<String> imagenesUrl = new ArrayList<>();
    private String categoria;
    private ImageAdapter adapter;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupRecyclerView();
        setupViewModel();
        setupCategorySpinner();
        setupGalleryLauncher();
        binding.btnUploadPost.setOnClickListener(v -> publicarPost());
//        viewModel = new ViewModelProvider(this).get(PostViewModel.class);
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
    }

    private void setupRecyclerView() {
        adapter = new ImageAdapter(imagenesUrl, this);
        binding.recyclerViewPost.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerViewPost.setAdapter(adapter);
        updateRecyclerViewVisibility();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(PostViewModel.class);
        viewModel.getPostSuccess().observe(this, success -> {
            String message = success.toString().equals("true") ? "Publicación exitosa" : "Error al publicar";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            if (success.toString().equals("true")) {
                finish();
            }
        });
    }

    private void setupCategorySpinner() {
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupGalleryLauncher() {

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                if (imageUri != null && imagenesUrl.size() < MAX_IMAGES) {
                    ImageUtils.subirImagenAParse(PostActivity.this, imageUri, new ImageUtils.ImageUploadCallback() {
                        @Override
                        public void onSuccess(String imageUrl) {
                            Log.d("PostActivity", "Imagen subida con éxito: " + imageUrl);
                            imagenesUrl.add(imageUrl);
                            adapter.notifyDataSetChanged();
                            updateRecyclerViewVisibility();
                        }
                        @Override
                        public void onFailure(Exception e) {
                            Log.e("PostActivity", "Error al subir la imagen: " + e.getMessage());
                            Toast.makeText(PostActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                    }
                    });
                } else if (imagenesUrl.size() >= MAX_IMAGES) {
                    Toast.makeText(PostActivity.this, "No puedes subir mas de 3 imagenes", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.uploadImage.setOnClickListener(v -> {
            Log.d("PostActivity", "Botón de subir imagen clickeado");
            ImageUtils.pedirPermisos(PostActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE);
        });
    }

    private void publicarPost() {
        String titulo = binding.etUploadTitulo.getText().toString().trim();
        String descripcion = binding.etUploadDescripcion.getText().toString().trim();
        String duracion = binding.etUploadDuraciona.getText().toString().trim();
        String presupuesto = binding.etUploadPresupuesto.getText().toString().trim();

        //validar campos
        if(!Validaciones.validarTexto(titulo)){
            binding.etUploadTitulo.setError("Titulo inválido");
            return;
        }

        if(!Validaciones.validarTexto(descripcion)){
            binding.etUploadDescripcion.setError("Descripcion inválida");
            return;
        }

        boolean duracionValida = Validaciones.validarNumero(String.valueOf(duracion));
        if(duracionValida){
            binding.etUploadDescripcion.setError("Duración inválida");
        }

        double presupuestoValido;
        try {
            presupuestoValido = Double.parseDouble(presupuesto);
        } catch (NumberFormatException e) {
            binding.etUploadPresupuesto.setError("Presupuesto inválido");
            return;
        }

        Post post = new Post(titulo, descripcion, Integer.parseInt(duracion), categoria, presupuestoValido, new ArrayList<>(imagenesUrl));
        viewModel.publicarPost(post);
    }

    private void updateRecyclerViewVisibility() {
        boolean hasImages = !imagenesUrl.isEmpty();
        binding.recyclerViewPost.setVisibility(hasImages ? View.VISIBLE : View.GONE);
        binding.uploadImage.setVisibility(hasImages ? View.GONE : View.VISIBLE);
    }

    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showToast(String message) {
        Toast.makeText(PostActivity.this, message, Toast.LENGTH_SHORT).show();
    }


}
