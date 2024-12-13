package com.murek.appsocial.view;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.murek.appsocial.R;
import com.murek.appsocial.adapters.ImageSliderAdapter;
import com.murek.appsocial.adapters.TransformerAdapter;
import com.murek.appsocial.databinding.ActivityPostDetailBinding;
import com.murek.appsocial.model.User;
import com.murek.appsocial.viewModel.PostDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {
    private ActivityPostDetailBinding binding;
    private PostDetailViewModel viewModel;
    private String postId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(PostDetailViewModel.class);
        postId=getIntent().getStringExtra("idPost");
        detailInfo();
        setupObservers();
        if (postId != null) {
            viewModel.fetchComments(postId);
        }
        binding.fabChat.setOnClickListener(v -> showDialogComment());
    }

    /** CODIGO VIEJO (LE ESTOY PASANDO OBJETO)....
    private void detailInfo() {
        User usuario = (User) getIntent().getSerializableExtra("usuario");
        if (usuario != null) {
            binding.nameUser.setText(usuario.getUserName());
            binding.emailUser.setText(usuario.getUserEmail());
            binding.insta.setText(usuario.getRedSocial());

            String fotoUrl = usuario.getUserFotoPerfil();
            if (fotoUrl != null) {
                Picasso.get()
                        .load(fotoUrl)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(binding.circleImageView);
            } else {
                binding.circleImageView.setImageResource(R.drawable.ic_person);
            }
            ArrayList<String> urls = getIntent().getStringArrayListExtra("imagenes");
            String titulo = getString(R.string.lugar) + getIntent().getStringExtra("titulo");
            binding.lugar.setText(titulo);
            String categoria = "Categoria: " + getIntent().getStringExtra("categoria");
            binding.categoria.setText(categoria);
            String comentario = "Comentario:  " + getIntent().getStringExtra("descripcion");
            binding.description.setText(comentario);
            String duracion = "Duración: " + getIntent().getIntExtra("duracion", 0) + " día/s";
            binding.duracion.setText(duracion);
            String presupuesto = "Presupuesto: U$ " + getIntent().getDoubleExtra("presupuesto", 0.0);
            binding.presupuesto.setText(presupuesto);
            if (urls != null && !urls.isEmpty()) {

                ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(urls);
                binding.viewPager.setAdapter(imageSliderAdapter);
                binding.viewPager.setPageTransformer(new TransformerAdapter());

                // Conexion TabLayout con ViewPager2
                new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
                }).attach();
            }
        }
    }*/

    private void detailInfo() {

        binding.nameUser.setText(getIntent().getStringExtra("username"));
        binding.emailUser.setText(getIntent().getStringExtra("email"));
        binding.insta.setText(getIntent().getStringExtra("redsocial"));

        String fotoUrl = getIntent().getStringExtra("foto_perfil");
        if (fotoUrl != null) {
            Picasso.get()
                    .load(fotoUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(binding.circleImageView);
        } else {
            binding.circleImageView.setImageResource(R.drawable.ic_person);
        }

        ArrayList<String> urls = getIntent().getStringArrayListExtra("imagenes");
        String titulo = "Lugar: " + getIntent().getStringExtra("titulo");
        binding.lugar.setText(titulo);
        String categoria = "Categoria: " + getIntent().getStringExtra("categoria");
        binding.categoria.setText(categoria);
        String comentario = "descripción: " + getIntent().getStringExtra("descripcion");
        binding.description.setText(comentario);
        String duracion = "Duración: " + getIntent().getIntExtra("duracion", 0) + " día/s";
        binding.duracion.setText(duracion);
        String presupuesto = "Presupuesto: U$ " + getIntent().getDoubleExtra("presupuesto", 0.0);
        binding.presupuesto.setText(presupuesto);

        if (urls != null && !urls.isEmpty()) {
            ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(urls);
            binding.viewPager.setAdapter(imageSliderAdapter);
            binding.viewPager.setPageTransformer(new TransformerAdapter());

            // Conexión TabLayout con ViewPager2
            new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            }).attach();
        }
    }

    private void showDialogComment() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PostDetailActivity.this);
        alert.setTitle("¡COMENTARIO!");
        alert.setMessage("Ingresa tu comentario: ");

        EditText editText = new EditText(PostDetailActivity.this);
        editText.setHint("Texto");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        editText.setLayoutParams(params);
        params.setMargins(36, 0, 36, 36);


        RelativeLayout container = new RelativeLayout(PostDetailActivity.this);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(relativeParams);
        container.addView(editText);
        alert.setView(container);


        alert.setPositiveButton("Ok", (dialog, which) -> {
            String value = editText.getText().toString().trim();
            if (!value.isEmpty()) {

                viewModel.saveComment(postId, value);
            } else {
                Toast.makeText(PostDetailActivity.this, "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancelar", (dialog, which) -> {

            dialog.dismiss();
        });


        alert.show();
    }

    private void setupObservers() {
        viewModel.getCommentsLiveData().observe(this, comments -> {

            // updateUI(comments);
        });


        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}