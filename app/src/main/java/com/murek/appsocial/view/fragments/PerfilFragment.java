package com.murek.appsocial.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.murek.appsocial.R;
import com.murek.appsocial.adapters.PostAdapter;
import com.murek.appsocial.databinding.FragmentPerfilBinding;
import com.murek.appsocial.model.User;
import com.murek.appsocial.util.ImageUtils;
import com.murek.appsocial.util.Validaciones;
import com.murek.appsocial.view.MainActivity;
import com.murek.appsocial.viewModel.PostViewModel;
import com.murek.appsocial.viewModel.UserViewModel;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private PostViewModel postViewModel;
    private PostAdapter postAdapter;
    private UserViewModel userViewModel;
    private User currentUser;
    private LinearLayout layoutActualizarDatos;
    private int cont = 0;

    public PerfilFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        setupMenu();
        setupToolbar();
        displayUserInfo();
        fetchPostCount();
        setupGalleryLauncher();
        setupProfileImageClick();
        setupRecyclerView();
        observeUserPosts();
        setupUpdateProfileFields();
        setupTextInputListeners();
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

    // Método para mostrar la información del usuario
    private void displayUserInfo() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            binding.nameUser.setText(currentUser.getUsername());
            binding.emailUser.setText(currentUser.getEmail());
            binding.cantPost.setText(String.valueOf(currentUser.getInt("posts")));
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

    // Método para manejar el clic en la imagen de perfil
    private void setupProfileImageClick() {
        binding.circleImageView.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ImageUtils.pedirPermisos(requireActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            ImageUtils.openGallery(requireContext(), galleryLauncher);
        });
    }

    // Manejar la selección de una imagen
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

    // Método para obtener la cantidad de posteos del usuario
    private void fetchPostCount() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
            query.whereEqualTo("user", currentUser);
            query.countInBackground((count, e) -> {
                if (e == null) {
                    binding.cantPost.setText(String.valueOf(count));
                } else {
                    binding.cantPost.setText("0");
                }
            });
        } else {
            binding.cantPost.setText("0");
        }
    }

    // Método para configurar el RecyclerView y el adaptador para los posteos del usuario en el perfil
    private void setupRecyclerView() {
        binding.recyclerViewPerfil.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new PostAdapter(new ArrayList<>()); // Iniciar con lista vacía
        binding.recyclerViewPerfil.setAdapter(postAdapter);
    }

    private void observeUserPosts() {
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.getUserPostList(0).observe(getViewLifecycleOwner(), posts -> {
            if (posts != null && !posts.isEmpty()) {
                postAdapter.updatePosts(posts); // Actualizar el adaptador con los posts
                binding.recyclerViewPerfil.setVisibility(View.VISIBLE);
                binding.tvNoPosts.setVisibility(View.GONE);
            } else {
                binding.recyclerViewPerfil.setVisibility(View.GONE);
                binding.tvNoPosts.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupUpdateProfileFields() {
        layoutActualizarDatos = binding.getRoot().findViewById(R.id.layout_updateUser);
        binding.btnUpdatePerfil.setOnClickListener(v -> {
            layoutActualizarDatos.setVisibility(View.VISIBLE);
//            binding.btnUpdatePerfil.setVisibility(View.GONE);
        });
        binding.btnUpdateUser.setOnClickListener(v -> {
            if (updateUser()) {
                layoutActualizarDatos.setVisibility(View.GONE);
//                binding.btnUpdatePerfil.setVisibility(View.VISIBLE);
            }
        });
        binding.btnCancelUpdateUser.setOnClickListener(v -> {
            layoutActualizarDatos.setVisibility(View.GONE);
//            binding.btnUpdatePerfil.setVisibility(View.VISIBLE);
        });
    }

    private void updateBtnManager() {
        boolean isChanged = false;
        if (currentUser != null) {
            if (!binding.etUpdateUsuario.getText().toString().equals(currentUser.getUserName())) {
                isChanged = true;
            }
            if (!binding.etUpdateMail.getText().toString().equals(currentUser.getUserEmail())) {
                isChanged = true;
            }
            if (!binding.etUpdatePass.getText().toString().equals(currentUser.getUserpassword())) {
                isChanged = true;
            }
        }
        if (isChanged) {
            binding.btnUpdateUser.setEnabled(true);
        } else {
            binding.btnUpdateUser.setEnabled(false);
        }
    }

    private void setupTextInputListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                cont++;
                if (cont > 3){
                    Log.d("PerfilFragment", "textChanged: ");
                    updateBtnManager();
                }
            }
        };
        binding.etUpdateUsuario.addTextChangedListener(textWatcher);
        binding.etUpdateMail.addTextChangedListener(textWatcher);
        binding.etUpdatePass.addTextChangedListener(textWatcher);
    }

    private boolean updateUser() {
        String usuario = binding.etUpdateUsuario.getText().toString().trim();
        String email = binding.etUpdateMail.getText().toString().trim();
        String pass = binding.etUpdatePass.getText().toString().trim();

        if (!Validaciones.validarTexto(usuario)) {
            showToast("Usuario incorrecto");
            return false;
        }
        if (!Validaciones.validarEmail(email)) {
            showToast("El correo no es válido");
            return false;
        }

        if (!Validaciones.controlarPassword(pass)) {
            showToast("Password incorrecto");
            return false;
        }

        if (currentUser != null) {
            User user = currentUser;
            user.setUserEmail(email);
            user.setUserName(usuario);
            user.setUserpassword(pass);

            userViewModel.updateUser(user).observe(getViewLifecycleOwner(),updateResult ->{
                if (updateResult) {
                    showToast("Actualización exitosa");
                    displayUserInfo();
                } else {
                    showToast("Actualización fallida");
                }
            });
        }
        return true;
    }

    private void showToast(String message) {
        if (message != null) {
            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show();
        }
    }

} //llave final


