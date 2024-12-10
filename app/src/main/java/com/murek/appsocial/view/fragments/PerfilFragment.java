package com.murek.appsocial.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.murek.appsocial.R;
import com.murek.appsocial.databinding.FragmentPerfilBinding;
import com.murek.appsocial.util.ImageUtils;
import com.murek.appsocial.view.MainActivity;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private ActivityResultLauncher<Intent> galleryLauncher;

    public PerfilFragment(){
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        setupMenu();
        setupToolbar();
        displayUserInfo();
        setupGalleryLauncher();
        setupProfileImageClick();
        return binding.getRoot();
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.itemClose) {
                    Toast.makeText(requireContext(), "Cerrar sesión", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.getRoot().findViewById(R.id.tools_filtro));
    }

    private void displayUserInfo() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            binding.nameUser.setText(currentUser.getUsername());
            binding.emailUser.setText(currentUser.getEmail());
//            binding.insta.setText(currentUser.getString("instagram"));

            String fotoUrl = currentUser.getString("foto_perfil");
            if (fotoUrl != null) {
                Picasso.get()
                        .load(fotoUrl)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(binding.circleImageView);
            } else {
                binding.circleImageView.setImageResource(R.drawable.ic_person);
            }
        } else {
            Toast.makeText(getContext(), "Usuario no logueado", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            handleImageSelection(imageUri);
                        }
                    }
                }
        );
    }

    private void setupProfileImageClick() {
        binding.circleImageView.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ImageUtils.pedirPermisos(requireActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            ImageUtils.openGallery(requireContext(), galleryLauncher);
        });
    }

    private void handleImageSelection(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().getContentResolver(), imageUri);
            binding.circleImageView.setImageBitmap(bitmap);

            ImageUtils.subirImagenAParse(requireContext(), imageUri, new ImageUtils.ImageUploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {

                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        currentUser.put("foto_perfil", imageUrl);
                        currentUser.saveInBackground(e -> {
                            if (e == null) {
                                Toast.makeText(requireContext(), "Foto subida correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "Error al guardar la URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(requireContext(), "Error al subir la foto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            Log.e("PerfilFragment", "Error al manejar la imagen: " + e.getMessage(), e);
            Toast.makeText(requireContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }
}
