package com.murek.appsocial.view;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.murek.appsocial.R;
import com.murek.appsocial.adapters.ImageAdapter;
import com.murek.appsocial.databinding.ActivityPostBinding;
import com.murek.appsocial.model.Post;
import com.murek.appsocial.util.Validaciones;
import com.murek.appsocial.viewModel.PostViewModel;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostActivityOld extends AppCompatActivity {

    private ActivityPostBinding binding;
    private PostViewModel postViewModel;
    private static final int REQUEST_IMAGE = 1;
    private List<String> imagenesUrl = new ArrayList<>();
    private String categoria;
    private ImageAdapter adapter;
    private ActivityResultLauncher<Intent> galleryLauncher;

    //eliminar?
    private Uri uri;
    private String idUser;
    private int cant = 0;

/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.getImagenesUrl().observe(this, imagenesUrl -> {
            adapter.updateImages(imagenesUrl);
        });

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerView.setAdapter(new ImageAdapter(imagenesUrl, this));
        binding.recyclerView.setVisibility(View.GONE);
        setupCategorySpinner();
        setupViewModel();
        binding.uploadImage.setOnClickListener(v -> pedirPermisos());
        binding.btnUploadPost.setOnClickListener(v -> publicarPost());
        adapter = new ImageAdapter(imagenesUrl, this);
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupRecyclerView() {

    }

    private void setupViewModel() {
        PostViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        PostViewModel.getPostSuccess().observe(this, success -> {
            Toast.makeText(this, success? "Post publicado": "Error al publicar post", Toast.LENGTH_SHORT).show();
            Log.d("TAG#", "Post upload success: " + success);
            if (success) {
                Toast.makeText(this, "Post publicado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al publicar post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, getResources().getStringArray(R.array.categorias));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategoria.setAdapter(adapter);
        binding.spinnerCategoria.setSelection(0);
        binding.spinnerCategoria.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                categoria = (String)parentView.getItemAtPosition(position).toString();
                categoria = (String) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        }));
    }

    private void setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                uri = data.getData();
                subirImagenAParse();
            }
        });
    }

    private void publicarPost() {

        String titulo = binding.etUploadTitulo.getText().toString().trim();
        String descripcion = binding.etUploadDescripcion.getText().toString().trim();
        String duracion = binding.etUploadDuracion.getText().toString().trim();
        String presupuesto = binding.etUploadPresupuesto.getText().toString().trim();

        Log.d("TAG#", "En post - contenido: " + titulo+" "+" "+duracion+" "+categoria+" "+presupuesto+" "+imagenesUrl);

        if (!Validaciones.validarTexto(titulo)) {
            binding.etUploadTitulo.setError("El título es inválido");
            return;
        }

        if (descripcion.isEmpty()) {
            binding.etUploadDescripcion.setError("La descripción es inválida");
            return;
        }

        if (!Validaciones.validarNumero(duracion) == -1) {
            binding.etUploadDuracion.setError("La duración es inválida");
            return;
        }

        if (!Validaciones.validarNumero(presupuestoStr) == -1) {
            binding.etUploadPresupuesto.setError("El presupuesto no puede estar vacío");
            return;
        }

        double presupuesto;
        try {
            presupuesto = Double.parseDouble(presupuestoStr);
        } catch (NumberFormatException e) {
            binding.etUploadPresupuesto.setError("El presupuesto es inválido");
            return;
        }

        Log.d("TAG#", imagenesUrl.toString());
        Post post = new Post(titulo, descripcion, Integer.parseInt(duracion), categoria, presupuesto, imagenesUrl, idUser);
        PostViewModel.uploadPost(post);
    }

    private void updateRecyclerViewVisibility() {
        binding.recyclerView.setVisibility(imagenesUrl.isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                galleryLauncher.launch(intent);
            }
        }
    }


    private void subirImagenAParse() {
        if (uri != null) {
            try (ImputStream inputStream = getContentResolver().openInputStream(uri)) {
                byte[] imagenBytes = getBytesFormImputStream(inputStream);
                ParseFile parseFile = new ParseFile("image.jpg", imagenBytes);
                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    if (e == null) {
                        public void done(ParseException e) {
                            String imagenurl = parseFile.getUrl();
                            imageViewModel.agregarImagen(imagenurl);
                            imagenesUrl.add(imagenurl);
                        }
                    } else {
                        Toast.makeText(UploadActivity.this, "Error al subir imagen", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al procesar la imagen", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se ha seleccionado ninguna imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getBytesFormImputStream(ImputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            bytesArrayOutputStream.write(buffer, 0, bytesRead);
//            byteBuffer.write(buffer, 0, bytesRead);
        }
        return bytesArrayOutputStream.toByteArray();
    }

    private void pedirPermisos() {
        String permisos = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(this, permisos) != PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permisos}, REQUEST_IMAGE);
        }
        subirImagenAParse();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    uri = result.getData().getData();
                    cant++;
                    if (cant == 3) {
                        mostrarRecyclerViewEnLugarDeImagen();
                    }
                    onImageSelected(uri.toString());
                } else {
                    Toast.makeText(this, "No se ha seleccionado ninguna imagen", Toast.LENGTH_SHORT).show();
                }
            }
    );



    private void mostrarRecyclerViewEnLugarDeImagen() {
        binding.uploadImage.setVisibility(View.GONE);
        ViewGroup.LayoutParams layoutParams = binding.recyclerView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        binding.recyclerView.setLayoutParams(layoutParams);
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    public void onImageSelected(String imgPath) {
        updateRecyclerViewVisibility();
        adapter.notifyDataSetChanged();
    }


*/
}
