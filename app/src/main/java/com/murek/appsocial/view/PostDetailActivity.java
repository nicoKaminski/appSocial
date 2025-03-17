package com.murek.appsocial.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayoutMediator;
import com.murek.appsocial.R;
import com.murek.appsocial.adapters.ComentarioAdapter;
import com.murek.appsocial.adapters.ImageSliderAdapter;
import com.murek.appsocial.adapters.TransformerAdapter;
import com.murek.appsocial.databinding.ActivityPostDetailBinding;

import com.murek.appsocial.viewModel.PostDetailViewModel;
import com.murek.appsocial.viewModel.PostViewModel;

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {
    private ActivityPostDetailBinding binding;
    private PostDetailViewModel viewModel;
    private String postId;
    private PostViewModel postViewModel;
    private ComentarioAdapter comentarioAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new PostViewModel();
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(PostDetailViewModel.class);
        postId=getIntent().getStringExtra("idPost");
        detailInfo();
        setupObservers();
        if (postId != null) {
            viewModel.fetchComments(postId);
        }

        //profe:*******************************
        binding.recyclerComentarios.setLayoutManager(new LinearLayoutManager(this));
        comentarioAdapter = new ComentarioAdapter(new ArrayList<>());
        binding.recyclerComentarios.setAdapter(comentarioAdapter);

        // Observing comments
        viewModel.getCommentsLiveData().observe(this, comentarios -> {
            comentarioAdapter.setComentarios(comentarios);
            comentarioAdapter.notifyDataSetChanged();
        });

        String currentUser = ParseUser.getCurrentUser().getUsername();
        String perfilUserId = getIntent().getStringExtra("username");

        if (currentUser != null && currentUser.equals(perfilUserId)) {
            binding.btnEliminarPost.setVisibility(View.VISIBLE);
            binding.btnEliminarPost.setOnClickListener(v -> confirmaBorrar());
        } else {
            binding.btnEliminarPost.setVisibility(View.GONE);
        }
        //profe**********************************************
        binding.fabChat.setOnClickListener(v -> showDialogComment());
        manejarEventos();
    }

    private void detailInfo() {
        binding.nameUser.setText(getIntent().getStringExtra("username"));
        binding.emailUser.setText(getIntent().getStringExtra("email"));
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
        String titulo = getIntent().getStringExtra("titulo");
        binding.lugar.setText(titulo);
        String categoria = getIntent().getStringExtra("categoria");
        binding.categoria.setText(categoria);
        String comentario = getIntent().getStringExtra("descripcion");
        binding.description.setText(comentario);
        String duracion = getIntent().getIntExtra("duracion", 0) + " día/s";
        binding.duracion.setText(duracion);
        String presupuesto = "U$ " + getIntent().getDoubleExtra("presupuesto", 0.0);
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

    private void manejarEventos() {
        // Boton volver atras
        binding.circuloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        });
        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getSuccessLiveData().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void confirmaBorrar() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirmación");
        alert.setMessage("¿Estás seguro de que deseas eliminar este post?");
        alert.setPositiveButton("Eliminar", (dialog, which) -> viewModel.removePost(postId));
        alert.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        alert.show();
    }
}