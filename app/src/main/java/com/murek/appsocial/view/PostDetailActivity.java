package com.murek.appsocial.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayoutMediator;
import com.murek.appsocial.R;
import com.murek.appsocial.adapters.ImageSliderAdapter;
import com.murek.appsocial.adapters.TransformerAdapter;
import com.murek.appsocial.databinding.ActivityPostDetailBinding;
import com.murek.appsocial.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {
    private ActivityPostDetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        detailInfo();
    }

    private void detailInfo() {
        User u = (User) getIntent().getSerializableExtra("usuario");
        if (u != null) {
            binding.nameUser.setText(u.getUserName());
            binding.emailUser.setText(u.getUserEmail());
//            binding.insta.setText(u.getRedSocial());

            String fotoUrl = u.getUserFotoPerfil();
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
            String titulo = getString(R.string.lugar)+getIntent().getStringExtra("titulo");
            binding.lugar.setText( titulo);
            String categoria="Categoria: "+getIntent().getStringExtra("categoria");
            binding.categoria.setText(categoria);
            String comentario="Comentario:  "+getIntent().getStringExtra("descripcion");
            binding.description.setText(comentario);
            String duracion="Duración: "+getIntent().getIntExtra("duracion",0)+" día/s";
            binding.duracion.setText(duracion);
            String presupuesto="Presupuesto: U$ "+ getIntent().getDoubleExtra("presupuesto",0.0);
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
    }
}